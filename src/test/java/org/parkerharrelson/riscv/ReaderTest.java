package org.parkerharrelson.riscv;

import org.junit.jupiter.api.Test;
import org.parkerharrelson.riscv.core.Machine;
import org.parkerharrelson.riscv.core.Reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ReaderTest {

    @Test
    public void testReadElfFile() throws IOException {
        Machine machine = new Machine();
        Reader reader = new Reader();

        // Load the ELF file
        reader.readElfFile(machine.getMemory(), machine, "src/test/resources/test.elf");

        // Load the expected memory contents
        byte[] expectedMemory = Files.readAllBytes(Paths.get("src/test/resources/expected_memory.bin"));

        // Extract the relevant memory segment for comparison
        int vaddrStart = 0x1000; // Starting virtual address
        int vaddrEnd = vaddrStart + expectedMemory.length;
        byte[] loadedMemorySegment = Arrays.copyOfRange(machine.getMemory(), vaddrStart, vaddrEnd);

        // Compare the loaded memory with the expected memory
        assertArrayEquals(expectedMemory, loadedMemorySegment, "Memory contents do not match the expected output");
    }
}
