package org.parkerharrelson.riscv.util;

public class EmulatorConstants {

    // Machine Constants
    public static final int RAM_SIZE = 1_048_576;

    // Reader Constants
    public static final int ELF_HEADER_SIZE = 52;
    public static final int PROGRAM_HEADER_SIZE = 32;
    public static final int PT_LOAD = 1;

    EmulatorConstants() {

    }
}
