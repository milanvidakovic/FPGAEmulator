package emulator.src.alu;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ALU_REG_MXX extends Instruction {
	int type;

	public ALU_REG_MXX(short[] memory, int addr, int src, int dest, int type) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler(Instruction.getTypeStr(type) + this.sdest + ", [0x%04x]");
		this.type = type;
	}
	
	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = 0;
		switch (type) {
		case ADD: res = ctx.getReg(this.dest).val + ctx.memory[fix(this.argument) / 2]; break;
		case SUB: res = ctx.getReg(this.dest).val - ctx.memory[fix(this.argument) / 2]; break;
		case AND: res = ctx.getReg(this.dest).val & ctx.memory[fix(this.argument) / 2]; break;
		case OR : res = ctx.getReg(this.dest).val | ctx.memory[fix(this.argument) / 2]; break;
		case XOR: res = ctx.getReg(this.dest).val ^ ctx.memory[fix(this.argument) / 2]; break;
		case SHL: res = ctx.getReg(this.dest).val << ctx.memory[fix(this.argument) / 2]; break;
		case SHR: res = ctx.getReg(this.dest).val >> ctx.memory[fix(this.argument) / 2]; break;
		case MUL:	res = ctx.getReg(this.dest).val * ctx.memory[fix(this.argument) / 2]; 
					ctx.h.val = (short)((res & 0xffff0000) >> 16);
					break;
		case DIV: 	res = ctx.getReg(this.dest).val / ctx.memory[fix(this.argument) / 2]; 
					ctx.h.val = (short)(ctx.getReg(this.dest).val % ctx.memory[fix(this.argument) / 2]);
					break;
		default: throw new RuntimeException("Unsupported operation type: " + type);
		}
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.memory[fix(this.argument) / 2], ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 4;
	}
}
