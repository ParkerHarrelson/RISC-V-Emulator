package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.util.Instruction;

public class RegisterWriteback {

    private final IMachine machine;

    /**
     * Constructor for RegisterWriteback.
     *
     * @param machine The machine instance used for writeback stage.
     */
    public RegisterWriteback(IMachine machine) {
        this.machine = machine;
    }

    /**
     * Writes the result into the destination register and handles system calls.
     *
     * <p>
     * This method writes the result of the ALU operation into the destination
     * register. It also handles system calls for the ECALL instruction and
     * updates the program counter based on the instruction type and operation result.
     * </p>
     *
     * @param instruction The instruction to execute writeback for.
     */
    public void writebackInstruction(Instruction instruction) {
        int rd = instruction.getRd();
        int result = instruction.getResult();

        if (rd != 0) {
            machine.writeToRegister(rd, result);
        }

        if (instruction.getInst() == 0x73) {
            machine.handleSystemCall();
        }

        int opcode = instruction.getInst() & 0x7F;
        int pc = machine.getProgramCounter();

        switch (opcode) {
            case 0x6F: // J-type
                machine.setProgramCounter(pc + instruction.getDisp());
                break;
            case 0x63: // B-type
                if (instruction.getResult() != 0) {
                    machine.setProgramCounter(pc + instruction.getDisp());
                } else {
                    machine.setProgramCounter(pc + 4);
                }
                break;
            default: // Non-branching instructions
                machine.setProgramCounter(pc + 4);
                break;
        }
    }
}
