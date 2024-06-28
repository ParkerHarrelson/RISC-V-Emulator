package org.parkerharrelson.riscv.core;

import org.parkerharrelson.riscv.pipeline.*;

import java.io.IOException;

import static org.parkerharrelson.riscv.util.EmulatorConstants.RAM_SIZE;

/**
 * Machine core code class for Part 1 of RISC-V Emulator Project
 *
 * <p>
 * Contains logic for setting up the machine's memory (RAM) with
 * 1 MiB, the register file containing 32, 32 bit registers, the
 * and the program counter. Will be the main entry point into running
 * the emulator, with logic to run the entire pipeline after ELF file
 * contents are stored in memory.
 * </p>
 */
public class Machine implements IMachine {

    private final byte[] memory;
    private final int[] registers;
    private int pc;
    private final Reader reader;
    private final InstructionFetch fetchStage;
    private final InstructionDecode decodeStage;
    private final InstructionExecute executeStage;
    private final MemoryAccess memoryStage;
    private final RegisterWriteback writebackStage;

    public Machine() {
        registers = new int[32];
        pc = 0x0;
        memory = new byte[RAM_SIZE];
        reader = new Reader();
        fetchStage = new InstructionFetch(this);
        decodeStage = new InstructionDecode(this);
        executeStage = new InstructionExecute(this);
        memoryStage = new MemoryAccess(this);
        writebackStage = new RegisterWriteback(this);
    }

    /**
     * This method takes the filepath from the command line in the main method and passes
     * it on to the reader class where the contents are read into memory
     *
     * @param filepath The filepath of the ELF file to be read into memory
     * @throws IOException Exception to be thrown if there are any issues with reading the file into memory
     */
    public void loadContents(String filepath) throws IOException {
        reader.readElfFile(memory, this, filepath);
    }

    /**
     * Entry point for running the emulator. Run loop will take place in here.
     */
    public void run() {

        boolean tempTrue = true;
        while (tempTrue) {
            int instruction = fetchStage.fetchInstruction();
            decodeStage.decodeInstruction(instruction);
            executeStage.executeInstruction(instruction);
            memoryStage.accessMemory(instruction);
            writebackStage.writebackInstruction(instruction);
            tempTrue = false;
        }
    }

    /**
     * Method for fetching content from a specified valid register. x0 register always returns 0x0
     *
     * @param index The index of the register in which contents are being fetched.
     * @return The 4 byte contents of the register.
     */
    @Override
    public int getRegister(int index) {
        if (index == 0) {
            return 0x0;
        } else if (index >= registers.length || index < 0) {
            throw new IndexOutOfBoundsException("Attempting to access a register that does not exist.");
        } else {
            return registers[index];
        }
    }

    /**
     * Write value to specific register. If index is 0, do not write to register.
     *
     * @param index Index of the register that is being written to.
     * @param val The value that will be stored in the register specified.
     */
    @Override
    public void writeToRegister(int index, int val) {
        if (index < registers.length && index > 0) {
            registers[index] = val;
        } else if (index >= registers.length) {
            throw new IndexOutOfBoundsException("Attempting to write to an invalid register.");
        }
    }

    /**
     * Getter method for the contents of the program counter.
     *
     * @return The contents of the program counter.
     */
    @Override
    public int getProgramCounter() {
        return this.pc;
    }

    /**
     * Method for setting a new value in the program counter.
     *
     * @param programCounter The new contents that will be stored in the program counter.
     */
    @Override
    public void setProgramCounter(int programCounter) {
        this.pc = programCounter;
    }

    @Override
    public byte[] getMemory() {
        return memory;
    }

    /**
     * Handles system calls based on the value in the a7 register.
     * Will be called in the writeback stage.
     */
    @Override
    public void handleSystemCall() {
        int syscallNumber = getRegister(17); // a7
        switch (syscallNumber) {
            case 0: // Exit
                System.exit(getRegister(10)); // a0
                break;
            case 1: // Putchar
                System.out.print((char) getRegister(10)); // a0
                break;
            case 2: // Getchar
                try {
                    int input = System.in.read();
                    writeToRegister(10, input); // a0
                } catch (IOException e) {
                    System.err.println("Error occurred reading input: " + e.getMessage());
                }
                break;
            case 3: // Debug
                // Handle debug call
                break;
            default:
                throw new UnsupportedOperationException("Unknown system call: " + syscallNumber);
        }
    }

    /**
     * This method performs sign extension on a given value.
     * It extends the value to 32 bits based on the most significant bit  index provided.
     *
     * @param value The value to be sign extended.
     * @param significantBitIndex The index of the most significant bit (0-based).
     * @return The sign extended 32-bit value.
     */
    public static int signExtend(int value, int significantBitIndex) {
        int bitMask = 1 << significantBitIndex;
        return (value ^ bitMask) - bitMask;
    }
}
