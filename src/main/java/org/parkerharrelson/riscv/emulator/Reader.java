package org.parkerharrelson.riscv.emulator;

import java.io.FileInputStream;
import java.io.IOException;

import static org.parkerharrelson.riscv.constants.EmulatorConstants.*;

/**
 * ELF File Reading Class for Part 2 of RISC-V Emulator Project
 * <p>
 * Contains all logic for reading ELF file into memory, verification
 * of the contents of the file to ensure it is fit for this machine,
 * and loading correct entry point address into the program counter.
 *</p>
 */
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
        FileInputStream elfInputStream = new FileInputStream(filePath);
        byte[] header = new byte[ELF_HEADER_SIZE];

        // TODO: validate the header of the file and then read in the contents into memory

        elfInputStream.close();
    }

    private boolean isValidElfFile(byte[] header) {
        return false;
    }
}
