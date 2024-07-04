package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.util.Instruction;

public class MemoryAccess {

    private final IMachine machine;

    public MemoryAccess(IMachine machine) {
        this.machine = machine;
    }

    public void accessMemory(Instruction instruction) {

    }
}
