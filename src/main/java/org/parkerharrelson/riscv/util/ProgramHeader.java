package org.parkerharrelson.riscv.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ProgramHeader {
    public int p_type;
    public int p_offset;
    public int p_vaddr;
    public int p_paddr;
    public int p_filesz;
    public int p_memsz;
    public int p_flags;
    public int p_align;

    public static ProgramHeader fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        ProgramHeader header = new ProgramHeader();
        header.p_type = buffer.getInt();
        header.p_offset = buffer.getInt();
        header.p_vaddr = buffer.getInt();
        header.p_paddr = buffer.getInt();
        header.p_filesz = buffer.getInt();
        header.p_memsz = buffer.getInt();
        header.p_flags = buffer.getInt();
        header.p_align = buffer.getInt();
        return header;
    }
}
