package org.parkerharrelson.riscv.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ElfHeader {
    public int e_ident0;
    public int e_ident1;
    public int e_ident2;
    public int e_ident3;
    public int e_bitsize;
    public int e_endian;
    public int e_filever;
    public int e_osabi;
    public int e_abiver;
    public int e_type;
    public int e_machine;
    public int e_version;
    public int e_entry;
    public int e_phoff;
    public int e_shoff;
    public int e_flags;
    public int e_ehsize;
    public int e_phentsize;
    public int e_phnum;
    public int e_shentsize;
    public int e_shnum;
    public int e_shstrndx;

    public static ElfHeader fromBytes(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        ElfHeader header = new ElfHeader();
        header.e_ident0 = buffer.get();
        header.e_ident1 = buffer.get();
        header.e_ident2 = buffer.get();
        header.e_ident3 = buffer.get();
        header.e_bitsize = buffer.get();
        header.e_endian = buffer.get();
        header.e_filever = buffer.get();
        header.e_osabi = buffer.get();
        header.e_abiver = buffer.get();
        buffer.position(buffer.position() + 7); // Skip e_padding
        header.e_type = buffer.getShort();
        header.e_machine = buffer.getShort();
        header.e_version = buffer.getInt();
        header.e_entry = buffer.getInt();
        header.e_phoff = buffer.getInt();
        header.e_shoff = buffer.getInt();
        header.e_flags = buffer.getInt();
        header.e_ehsize = buffer.getShort();
        header.e_phentsize = buffer.getShort();
        header.e_phnum = buffer.getShort();
        header.e_shentsize = buffer.getShort();
        header.e_shnum = buffer.getShort();
        header.e_shstrndx = buffer.getShort();
        return header;
    }
}
