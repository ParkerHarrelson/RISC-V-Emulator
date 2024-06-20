package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;

public class InstructionFetch {

    private final IMachine machine;

    public InstructionFetch(IMachine machine) {
        this.machine = machine;
    }

    public int fetchInstruction() {
        return 0;
    }
}
