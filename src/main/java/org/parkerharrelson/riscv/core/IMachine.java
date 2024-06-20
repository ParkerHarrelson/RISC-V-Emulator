package org.parkerharrelson.riscv.core;

public interface IMachine {
    int getRegister(int index);
    void writeToRegister(int index, int val);
    int getProgramCounter();
    void setProgramCounter(int programCounter);
    byte[] getMemory();
    void handleSystemCall();
}
