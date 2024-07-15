package org.parkerharrelson.riscv.pipeline;

import org.parkerharrelson.riscv.core.IMachine;
import org.parkerharrelson.riscv.core.Machine;
import org.parkerharrelson.riscv.util.AluOp;
import org.parkerharrelson.riscv.util.Instruction;

public class InstructionDecode {

    private final IMachine machine;

    /**
     * Constructor for InstructionDecode.
     *
     * @param machine The machine instance used for decoding instructions.
     */
    public InstructionDecode(IMachine machine) {
        this.machine = machine;
    }

    /**
     * Decodes the given instruction and prepares the necessary fields
     * for further stages in the pipeline.
     *
     * <p>
     * This method identifies the instruction type based on the opcode,
     * extracts the relevant fields, and sets the appropriate fields in
     * the Instruction object.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    public void decodeInstruction(Instruction instruction) {
        int inst = instruction.getInst();
        int opcode = inst & 0x7F;

        switch (opcode) {
            case 0x33: // R-type
                decodeRType(instruction);
                break;
            case 0x13: // I-type (OP-IMM)
                decodeIType(instruction);
                break;
            case 0x03: // I-type (LOAD)
                decodeLoadType(instruction);
                break;
            case 0x23: // S-type
                decodeSType(instruction);
                break;
            case 0x63: // B-type
                decodeBType(instruction);
                break;
            case 0x37: // U-type (LUI)
            case 0x17: // U-type (AUIPC)
                decodeUType(instruction);
                break;
            case 0x6F: // J-type (JAL)
                decodeJType(instruction);
                break;
            case 0x67: // JALR (I-type)
                decodeJALRType(instruction);
                break;
            case 0x73: // SYSTEM
                decodeSystemType(instruction);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported opcode: " + opcode);
        }
    }

    /**
     * Decodes an R-type instruction.
     *
     * <p>
     * This method extracts the destination register (rd), the source registers (rs1 and rs2),
     * and the ALU operation (aluop) based on the funct3 and funct7 fields.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeRType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        int funct3 = (inst >> 12) & 0x7;
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(machine.getRegister((inst >> 20) & 0x1F));
        int funct7 = (inst >> 25) & 0x7F;

        switch (funct3) {
            case 0x0:
                if (funct7 == 0x20) {
                    instruction.setAluOp(AluOp.Sub);
                } else if (funct7 == 0x01) {
                    instruction.setAluOp(AluOp.Mul);
                } else {
                    instruction.setAluOp(AluOp.Add);
                }
                break;
            case 0x1:
                instruction.setAluOp(AluOp.LeftShift);
                break;
            case 0x2:
                instruction.setAluOp(AluOp.Slt);
                break;
            case 0x3:
                instruction.setAluOp(AluOp.SltU);
                break;
            case 0x4:
                if (funct7 == 0x01) {
                    instruction.setAluOp(AluOp.Div);
                } else {
                    instruction.setAluOp(AluOp.Xor);
                }
                break;
            case 0x5:
                instruction.setAluOp((funct7 == 0x20) ? AluOp.RightShiftA : AluOp.RightShiftL);
                break;
            case 0x6:
                if (funct7 == 0x01) {
                    instruction.setAluOp(AluOp.Rem);
                } else {
                    instruction.setAluOp(AluOp.Or);
                }
                break;
            case 0x7:
                if (funct7 == 0x01) {
                    instruction.setAluOp(AluOp.RemU);
                } else if (funct7 == 0x20) {
                    instruction.setAluOp(AluOp.DivU);
                } else {
                    instruction.setAluOp(AluOp.And);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported funct3: " + funct3);
        }
    }

    /**
     * Decodes an I-type instruction.
     *
     * <p>
     * This method extracts the destination register (rd), the source register (rs1),
     * and the immediate value. It sets the appropriate ALU operation based on the funct3 field.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeIType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        int funct3 = (inst >> 12) & 0x7;
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(Machine.signExtend(inst >> 20, 11));

        switch (funct3) {
            case 0x0: // ADDI
                instruction.setAluOp(AluOp.Add);
                break;
            case 0x2: // SLTI
                instruction.setAluOp(AluOp.Slt);
                break;
            case 0x3: // SLTIU
                instruction.setAluOp(AluOp.SltU);
                break;
            case 0x4: // XORI
                instruction.setAluOp(AluOp.Xor);
                break;
            case 0x6: // ORI
                instruction.setAluOp(AluOp.Or);
                break;
            case 0x7: // ANDI
                instruction.setAluOp(AluOp.And);
                break;
            case 0x1: // SLLI
                instruction.setAluOp(AluOp.LeftShift);
                break;
            case 0x5: // SRLI, SRAI
                instruction.setAluOp(((inst >> 30) & 0x1) == 1 ? AluOp.RightShiftA : AluOp.RightShiftL);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported funct3: " + funct3);
        }
    }

    /**
     * Decodes a load-type instruction (I-type format).
     *
     * <p>
     * This method extracts the destination register (rd), the source register (rs1),
     * and the immediate value. It sets the memop field to indicate a load operation
     * and sets the ALU operation to addition.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeLoadType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        int funct3 = (inst >> 12) & 0x7;
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(Machine.signExtend(inst >> 20, 11));

        switch (funct3) {
            case 0x0: // LB
            case 0x1: // LH
            case 0x2: // LW
            case 0x4: // LBU
            case 0x5: // LHU
                instruction.setMemop(1);
                instruction.setAluOp(AluOp.Add);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported load funct3: " + funct3);
        }
    }

    /**
     * Decodes an S-type instruction.
     *
     * <p>
     * This method extracts the source registers (rs1 and rs2) and the immediate value.
     * It sets the memop field to indicate a store operation and sets the ALU operation to addition.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeSType(Instruction instruction) {
        int inst = instruction.getInst();
        int value = ((inst >> 7) & 0x1F) | ((inst >> 25) << 5);
        instruction.setDisp(Machine.signExtend(value, 11));
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(machine.getRegister((inst >> 20) & 0x1F));
        instruction.setMemop(2);
    }

    /**
     * Decodes a B-type instruction.
     *
     * <p>
     * This method extracts the source registers (rs1 and rs2) and the immediate value.
     * It sets the ALU operation to compare.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeBType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setDisp(Machine.signExtend(((inst >> 7) & 0x1E) | ((inst >> 25) << 5) | ((inst & 0x80) << 4) | ((inst >> 31) << 12), 12));
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(machine.getRegister((inst >> 20) & 0x1F));
        instruction.setAluOp(AluOp.Cmp);
    }

    /**
     * Decodes a U-type instruction.
     *
     * <p>
     * This method extracts the destination register (rd) and the immediate value.
     * It sets the ALU operation to addition.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeUType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        instruction.setRight(inst & 0xFFFFF000);
        instruction.setLeft((inst & 0x7F) == 0x17 ? machine.getProgramCounter() : 0);
        instruction.setAluOp(AluOp.Add);
    }

    /**
     * Decodes a J-type instruction.
     *
     * <p>
     * This method extracts the destination register (rd) and the immediate value.
     * It sets the ALU operation to no operation (Nop).
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeJType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        instruction.setDisp(Machine.signExtend(((inst >> 21) & 0x3FF) | ((inst >> 20) & 0x1) | ((inst >> 12) & 0xFF) | ((inst >> 31) << 20), 20));
        instruction.setAluOp(AluOp.Nop);
    }

    /**
     * Decodes a JALR-type instruction (I-type format).
     *
     * <p>
     * This method extracts the destination register (rd), the source register (rs1),
     * and the immediate value. It sets the ALU operation to addition.
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeJALRType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRd((inst >> 7) & 0x1F);
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRight(Machine.signExtend(inst >> 20, 11));
        instruction.setAluOp(AluOp.Add);
    }

    /**
     * Decodes a System type instruction.
     *
     * <p>
     * This method extracts the source register (rs1), the immediate value, and the destination register (rd).
     * It sets the ALU operation to no operation (Nop).
     * </p>
     *
     * @param instruction The instruction to decode.
     */
    private void decodeSystemType(Instruction instruction) {
        int inst = instruction.getInst();
        instruction.setRight(Machine.signExtend(inst >> 20, 11));
        instruction.setLeft(machine.getRegister((inst >> 15) & 0x1F));
        instruction.setRd((inst >> 7) & 0x1F);
        instruction.setAluOp(AluOp.Nop);
    }
}
