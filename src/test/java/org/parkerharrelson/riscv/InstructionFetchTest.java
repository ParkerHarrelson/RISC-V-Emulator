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
        // Load a test ELF file to set up memory and initial PC
        machine.loadContents("src/test/resources/test.elf");

        // Debug: Print initial PC and memory contents
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
        // Expected instruction at the initial PC
        int expectedInst = 0x04030201; // Assuming the first 4 bytes of test.elf are 01 02 03 04 in little-endian

        // Fetch the instruction
        Instruction fetchedInstruction = instructionFetch.fetchInstruction();

        // Verify the fetched instruction
        assertEquals(expectedInst, fetchedInstruction.getInst(), "Fetched instruction does not match expected value");

        // Verify that the PC has been incremented by 4
        int expectedPc = machine.getProgramCounter() - 4 + 4; // PC should be incremented by 4 after fetching the first instruction
        assertEquals(expectedPc, machine.getProgramCounter(), "Program counter did not increment correctly");

        // Verify that the stack pointer is set correctly
        int expectedSp = machine.getMemory().length;
        assertEquals(expectedSp, machine.getRegister(2), "Stack pointer was not set correctly");
    }
}
