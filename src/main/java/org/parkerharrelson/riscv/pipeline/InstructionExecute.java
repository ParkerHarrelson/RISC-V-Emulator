package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;

public class InstructionExecute {

    private final IMachine machine;

    public InstructionExecute(IMachine machine) {
        this.machine = machine;
    }
}
