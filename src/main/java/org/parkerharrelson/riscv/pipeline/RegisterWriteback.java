package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.util.Instruction;

public class RegisterWriteback {

    private final IMachine machine;

    public RegisterWriteback(IMachine machine) {
        this.machine = machine;
    }

    public void writebackInstruction(Instruction instruction) {

    }
}
