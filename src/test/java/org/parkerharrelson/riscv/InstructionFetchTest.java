package org.parkerharrelson.riscv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parkerharrelson.riscv.core.Machine;
import org.parkerharrelson.riscv.pipeline.InstructionFetch;
import org.parkerharrelson.riscv.util.Instruction;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstructionFetchTest {

    private Machine machine;
    private InstructionFetch instructionFetch;

    @BeforeEach
    public void setUp() throws IOException {
        machine = new Machine();
        instructionFetch = new InstructionFetch(machine);
        machine.loadContents("src/test/resources/test.elf");

        int pc = machine.getProgramCounter();
        byte[] memory = machine.getMemory();
        System.out.printf("Initial PC: %d%n", pc);
        System.out.printf("Memory at PC: %02x %02x %02x %02x%n", memory[pc], memory[pc + 1], memory[pc + 2], memory[pc + 3]);

        for (int i = 0; i < memory.length; i++) {
            if (memory[i] != 0) {
                System.out.println("Index " + i + ": " + memory[i]);
            }
        }
    }

    @Test
    public void testFetchInstruction() {
        int expectedInst = 0x04030201;

        Instruction fetchedInstruction = instructionFetch.fetchInstruction();

        assertEquals(expectedInst, fetchedInstruction.getInst(), "Fetched instruction does not match expected value");

        int expectedPc = machine.getProgramCounter() - 4 + 4;
        assertEquals(expectedPc, machine.getProgramCounter(), "Program counter did not increment correctly");

        int expectedSp = machine.getMemory().length;
        assertEquals(expectedSp, machine.getRegister(2), "Stack pointer was not set correctly");
    }
}
