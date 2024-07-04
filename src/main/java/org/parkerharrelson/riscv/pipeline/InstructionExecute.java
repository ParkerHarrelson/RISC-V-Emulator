package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.util.Instruction;

public class InstructionExecute {

    private final IMachine machine;

    public InstructionExecute(IMachine machine) {
        this.machine = machine;
    }

    public void executeInstruction(Instruction instruction) {

    }
}
