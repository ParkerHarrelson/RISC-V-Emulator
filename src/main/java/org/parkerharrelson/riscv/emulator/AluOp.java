package org.parkerharrelson.riscv.emulator;

public enum AluOp {

    Nop,         // No operation
    Add,         // Signed addition
    Sub,         // Signed subtraction
    Mul,         // Signed multiplication
    Div,         // Signed division
    DivU,        // Unsigned division
    Rem,         // Signed remainder (modulo)
    RemU,        // Unsigned remainder (modulo)
    LeftShift,   // Logical left shift (<<)
    RightShiftA, // Arithmetic right shift (>>)
    RightShiftL, // Logical right shift (>>)
    Or,          // Bitwise inclusive OR (|)
    Xor,         // Bitwise exclusive OR (^)
    And,         // Bitwise AND (&)
    Slt,         // Set on less than
    SltU,        // Set on less than unsigned
    Cmp,         // Compare
}
