package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.util.Instruction;

public class InstructionExecute {

    /**
     * Executes the given instruction by performing the ALU operation.
     *
     * <p>
     * This method performs the ALU operation specified by the aluop field
     * in the Instruction object and stores the result in the result field.
     * For load and store instructions, it calculates the memory address.
     * </p>
     *
     * @param instruction The instruction to execute.
     */
    public void executeInstruction(Instruction instruction) {
        int left = instruction.getLeft();
        int right = instruction.getRight();
        int result = switch (instruction.getAluop()) {
            case Add -> left + right;
            case Sub -> left - right;
            case Mul -> left * right;
            case Div -> left / right;
            case DivU -> Integer.divideUnsigned(left, right);
            case Rem -> left % right;
            case RemU -> Integer.remainderUnsigned(left, right);
            case LeftShift -> left << right;
            case RightShiftA -> left >> right;
            case RightShiftL -> left >>> right;
            case Or -> left | right;
            case Xor -> left ^ right;
            case And -> left & right;
            case Slt -> (left < right) ? 1 : 0;
            case SltU -> (Integer.compareUnsigned(left, right) < 0) ? 1 : 0;
            case Cmp -> ((left == right) ? 0b001 : 0) |
                    ((left < right) ? 0b010 : 0) |
                    ((Integer.compareUnsigned(left, right) < 0) ? 0b100 : 0);
            case Nop -> 0;
        };

        if (instruction.getMemop() == 1 || instruction.getMemop() == 2) {
            result = left + right;
        }

        instruction.setResult(result);
    }
}
