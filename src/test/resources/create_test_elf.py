import struct

# ELF header fields
elf_header = struct.pack(
    '<16sHHIIIIIHHHHHH',
    b'\x7fELF\x01\x01\x01\x00' + b'\x00' * 8,  # e_ident (16 bytes)
    2,           # e_type (2 bytes)
    243,         # e_machine (2 bytes)
    1,           # e_version (4 bytes)
    0x54,        # e_entry (4 bytes)
    0x34,        # e_phoff (4 bytes)
    0,           # e_shoff (4 bytes)
    0,           # e_flags (4 bytes)
    0x34,        # e_ehsize (2 bytes)
    0x20,        # e_phentsize (2 bytes)
    1,           # e_phnum (2 bytes)
    0,           # e_shentsize (2 bytes)
    0,           # e_shnum (2 bytes)
    0            # e_shstrndx (2 bytes)
)

# Program header fields
program_header = struct.pack(
    '<IIIIIIII',
    1,           # p_type (PT_LOAD) (4 bytes)
    0x54,        # p_offset (4 bytes)
    0x1000,      # p_vaddr (4 bytes)
    0x1000,      # p_paddr (4 bytes)
    0x10,        # p_filesz (4 bytes)
    0x10,        # p_memsz (4 bytes)
    0x7,         # p_flags (4 bytes)
    0x1000       # p_align (4 bytes)
)

program_data = [
    0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
    0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10
]

with open('test.elf', 'wb') as f:
    f.write(elf_header)
    f.write(program_header)
    f.write(bytearray(program_data))
