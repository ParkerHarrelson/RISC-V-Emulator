package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.util.Instruction;

public class InstructionFetch {

    private final IMachine machine;
    private boolean stackPointerInitialized = false;

    /**
     * Constructor for the InstructionFetch class.
     *
     * @param machine The machine instance to fetch instructions from.
     */
    public InstructionFetch(IMachine machine) {
        this.machine = machine;
    }

    /**
     * Fetches the next instruction from memory.
     * Initializes the stack pointer on the first fetch.
     * Updates the program counter after fetching the instruction.
     *
     * @return The fetched instruction encapsulated in an Instruction object.
     */
    public Instruction fetchInstruction() {
        if (!stackPointerInitialized) {
            machine.writeToRegister(2, machine.getMemory().length);
            stackPointerInitialized = true;
        }

        int pc = machine.getProgramCounter();
        byte[] memory = machine.getMemory();

        int instruction = ((memory[pc] & 0xFF)) |
                ((memory[pc + 1] & 0xFF) << 8) |
                ((memory[pc + 2] & 0xFF) << 16) |
                ((memory[pc + 3] & 0xFF) << 24);

        machine.setProgramCounter(pc + 4);

        return new Instruction(instruction);
    }
}
