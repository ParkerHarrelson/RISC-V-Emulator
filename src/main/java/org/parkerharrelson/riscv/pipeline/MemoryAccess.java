package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.core.Machine;
import org.parkerharrelson.riscv.util.Instruction;

public class MemoryAccess {

    private final IMachine machine;

    /**
     * Constructor for MemoryAccess.
     *
     * @param machine The machine instance used for memory access.
     */
    public MemoryAccess(IMachine machine) {
        this.machine = machine;
    }

    /**
     * Accesses memory for LOAD and STORE instructions.
     *
     * <p>
     * This method handles LOAD and STORE instructions by either storing data
     * to memory or loading data from memory. It also performs sign extension
     * or zero extension as needed.
     * </p>
     *
     * @param instruction The instruction to execute memory access for.
     */
    public void accessMemory(Instruction instruction) {
        if (instruction.getMemop() == 2) {
            storeToMemory(instruction);
        } else if (instruction.getMemop() == 1) {
            loadFromMemory(instruction);
        }
    }

    /**
     * Handles STORE instructions by storing data into memory.
     *
     * <p>
     * This method uses the calculated memory address (in result)
     * and the value to be stored (in right) to store data into memory.
     * </p>
     *
     * @param instruction The STORE instruction to execute.
     */
    private void storeToMemory(Instruction instruction) {
        int address = instruction.getResult();
        int value = instruction.getRight();
        byte[] memory = machine.getMemory();

        switch ((instruction.getInst() >> 12) & 0x7) {
            case 0x0: // SB
                memory[address] = (byte) value;
                break;
            case 0x1: // SH
                memory[address] = (byte) (value & 0xFF);
                memory[address + 1] = (byte) ((value >> 8) & 0xFF);
                break;
            case 0x2: // SW
                memory[address] = (byte) (value & 0xFF);
                memory[address + 1] = (byte) ((value >> 8) & 0xFF);
                memory[address + 2] = (byte) ((value >> 16) & 0xFF);
                memory[address + 3] = (byte) ((value >> 24) & 0xFF);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported store funct3: " + ((instruction.getInst() >> 12) & 0x7));
        }
    }

    /**
     * Handles LOAD instructions by loading data from memory.
     *
     * <p>
     * This method uses the calculated memory address (in result)
     * to load data from memory. It also performs sign or zero extension
     * as required by the instruction.
     * </p>
     *
     * @param instruction The LOAD instruction to execute.
     */
    private void loadFromMemory(Instruction instruction) {
        int address = instruction.getResult();
        byte[] memory = machine.getMemory();
        int loadedValue = switch ((instruction.getInst() >> 12) & 0x7) {
            case 0x0 -> // LB
                    Machine.signExtend(memory[address], 7);
            case 0x1 -> // LH
                    Machine.signExtend((memory[address] & 0xFF) | (memory[address + 1] << 8), 15);
            case 0x2 -> // LW
                    (memory[address] & 0xFF) | ((memory[address + 1] & 0xFF) << 8) | ((memory[address + 2] & 0xFF) << 16) | (memory[address + 3] << 24);
            case 0x4 -> // LBU
                    memory[address] & 0xFF;
            case 0x5 -> // LHU
                    (memory[address] & 0xFF) | ((memory[address + 1] & 0xFF) << 8);
            default ->
                    throw new UnsupportedOperationException("Unsupported load funct3: " + ((instruction.getInst() >> 12) & 0x7));
        };

        instruction.setResult(loadedValue);
    }
}
