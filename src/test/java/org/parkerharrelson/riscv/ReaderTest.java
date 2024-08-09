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

        reader.readElfFile(machine.getMemory(), machine, "src/test/resources/test.elf");

        byte[] expectedMemory = Files.readAllBytes(Paths.get("src/test/resources/expected_memory.bin"));

        int vaddrStart = 0x1000;
        int vaddrEnd = vaddrStart + expectedMemory.length;
        byte[] loadedMemorySegment = Arrays.copyOfRange(machine.getMemory(), vaddrStart, vaddrEnd);

        assertArrayEquals(expectedMemory, loadedMemorySegment, "Memory contents do not match the expected output");
    }
}
