package org.parkerharrelson.riscv.core;

import org.parkerharrelson.riscv.util.ElfHeader;
import org.parkerharrelson.riscv.util.ProgramHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.parkerharrelson.riscv.util.EmulatorConstants.*;

public class Reader {

    /**
     * Reads the ELF file and loads its contents into memory.
     *
     * @param memory  The byte array representing the machine's memory (RAM).
     * @param machine The machine instance to set the program counter.
     * @param filepath The path to the ELF file to be read.
     * @throws IOException Exception thrown if there are issues reading the file.
     */
    public void readElfFile(byte[] memory, IMachine machine, String filepath) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filepath, "r")) {

            byte[] elfHeaderData = new byte[ELF_HEADER_SIZE];
            file.readFully(elfHeaderData);
            ElfHeader elfHeader = ElfHeader.fromBytes(elfHeaderData);

            validateElfHeader(elfHeader);

            file.seek(elfHeader.e_phoff);
            for (int i = 0; i < elfHeader.e_phnum; i++) {
                byte[] programHeaderData = new byte[PROGRAM_HEADER_SIZE];
                file.readFully(programHeaderData);
                ProgramHeader programHeader = ProgramHeader.fromBytes(programHeaderData);

                if (programHeader.p_type == PT_LOAD) {
                    loadSegment(file, memory, programHeader.p_offset, programHeader.p_vaddr, programHeader.p_filesz, programHeader.p_memsz);
                }
            }

            machine.setProgramCounter(elfHeader.e_entry);
        }
    }

    /**
     * Validates the ELF header to ensure it conforms to the expected format.
     *
     * @param elfHeader The ELF header object to validate.
     * @throws IOException Exception thrown if the ELF header is invalid.
     */
    private void validateElfHeader(ElfHeader elfHeader) throws IOException {
        if (elfHeader.e_ident0 != 0x7F || elfHeader.e_ident1 != 'E' || elfHeader.e_ident2 != 'L' || elfHeader.e_ident3 != 'F') {
            throw new IOException("Invalid ELF file");
        }
        if (elfHeader.e_bitsize != 1) {
            throw new IOException("Invalid ELF class: not 32-bit");
        }
        if (elfHeader.e_endian != 1) {
            throw new IOException("Invalid ELF endian: not little-endian");
        }
        if (elfHeader.e_machine != 243) {
            throw new IOException("Invalid ELF target: not RISC-V");
        }
    }

    /**
     * Loads a segment from the ELF file into memory.
     *
     * @param file    The RandomAccessFile object to read the ELF file.
     * @param memory  The byte array representing the machine's memory.
     * @param offset  The offset in the file where the segment starts.
     * @param vaddr   The virtual address where the segment should be loaded.
     * @param filesz  The size of the segment in the file.
     * @param memsz   The size of the segment in memory.
     * @throws IOException Exception thrown if there are issues reading the segment or if it exceeds memory bounds.
     */
    private void loadSegment(RandomAccessFile file, byte[] memory, int offset, int vaddr, int filesz, int memsz) throws IOException {
        file.seek(offset);
        byte[] segment = new byte[filesz];
        file.readFully(segment);
        if (vaddr + filesz > memory.length) {
            throw new IOException("Segment exceeds memory bounds");
        }

        System.arraycopy(segment, 0, memory, vaddr, filesz);

        for (int i = filesz; i < memsz; i++) {
            memory[vaddr + i] = 0;
        }
    }
}
