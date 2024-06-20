package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;

public class RegisterWriteback {

    private final IMachine machine;

    public RegisterWriteback(IMachine machine) {
        this.machine = machine;
    }
}
