package org.parkerharrelson.riscv.emulator;

import java.io.IOException;

public class Machine {

    // 1 mebibyte size
    private static final int RAM_SIZE = 1_048_576;
    private final byte[] memory;
    private final int[] registers;
    private int pc;
    private final Reader reader;

    public Machine() {
        registers = new int[32];
        pc = 0x00000000;
        memory = new byte[RAM_SIZE];
        reader = new Reader();
    }

    /**
     * This method takes the filepath from the command line in the main method and passes
     * it on to the reader class where the contents are read into memory
     *
     * @param filepath The filepath of the ELF file to be read into memory
     * @throws IOException Exception to be thrown if there are any issues with reading the file into memory
     */
    public void loadContents(String filepath) throws IOException {
        reader.readElfFile(memory, filepath);
    }

    // Entry point for running the emulator. Run loop will take place in here.
    public void run() {

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

    // This is going to be a method that in the future will be used for fetching
    // from a specific register
    public int getRegister() {
        return registers[0];
    }

    // This is going to be a method where we write to a register, if that is how it works
    public void writeToRegister(int register, int val) {
        registers[register] = val;
    }

    // This is a getter for the program counter since it will be a private field
    // within the Machine class
    public int getProgramCounter() {
        return this.pc;
    }

    private void updateProgramCounter(int nextInstruction) {
        this.pc = nextInstruction;
    }
}
