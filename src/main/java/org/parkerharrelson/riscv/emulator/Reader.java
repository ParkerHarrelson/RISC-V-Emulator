package org.parkerharrelson.riscv.emulator;

import java.io.IOException;

public class Reader {

    /**
     * Reads the contents of an ELF file and loads them into the Machine's memory (RAM).
     * The file path is provided through the command line, and the method extracts the contents
     * and places them into their assigned locations in memory.
     *
     * @param memory The byte array representing the Machine's memory (RAM) where the ELF file's contents will be loaded.
     * @param filePath The path to the ELF file to be read.
     * @throws IOException If an I/O error occurs while reading the ELF file.
     */
    public void readElfFile(byte[] memory, String filePath) throws IOException {

    }

}
