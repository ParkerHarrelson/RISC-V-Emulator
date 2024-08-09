package org.parkerharrelson.riscv.util;

public class Instruction {

    public Instruction(int instruction) {
        this.inst = instruction;
        this.aluop = AluOp.Nop;
    }

    private int inst;   // The four-byte instruction
    private int left;   // The left operand
    private int right;  // The right operand
    private int disp;   // The displacement for BRANCH instructions or the value to store for STORE instructions.
    private int rd;     // The destination register index (0 - 31)

    private int memop;  // 0 = No mem op, 1 = Load mem op, 2 = Store mem op
    private AluOp aluop; // ALU operation
    private int result;  // Result of the ALU operation

    public int getInst() {
        return inst;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getDisp() {
        return disp;
    }

    public void setDisp(int disp) {
        this.disp = disp;
    }

    public int getRd() {
        return rd;
    }

    public void setRd(int rd) {
        this.rd = rd;
    }

    public int getMemop() {
        return memop;
    }

    public void setMemop(int memop) {
        this.memop = memop;
    }

    public AluOp getAluop() {
        return aluop;
    }

    public void setAluOp(AluOp aluop) {
        this.aluop = aluop;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
