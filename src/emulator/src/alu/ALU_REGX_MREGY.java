package emulator.src.alu;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ALU_REGX_MREGY extends Instruction {
	int type;
	
	public ALU_REGX_MREGY(short[] memory, int addr, int src, int dest, int type) {
		super(memory, addr, src, dest);
		super.setAssembler(Instruction.getTypeStr(type) + this.sdest + ", [" + this.ssrc + "]");
		this.type = type;
	}

	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = 0;
		switch (type) {
		case ADD: res = ctx.getReg(this.dest).val + ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case SUB: res = ctx.getReg(this.dest).val - ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case AND: res = ctx.getReg(this.dest).val & ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case OR : res = ctx.getReg(this.dest).val | ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case XOR: res = ctx.getReg(this.dest).val ^ ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case SHL: res = ctx.getReg(this.dest).val << ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case SHR: res = ctx.getReg(this.dest).val >> ctx.memory[fix(ctx.getReg(this.src).val) / 2]; break;
		case MUL:	res = ctx.getReg(this.dest).val * ctx.memory[fix(ctx.getReg(this.src).val) / 2]; 
					ctx.h.val = (short)((res & 0xffff0000) >> 16);
					break;
		case DIV: 	res = ctx.getReg(this.dest).val / ctx.memory[fix(ctx.getReg(this.src).val) / 2]; 
					ctx.h.val = (short)(ctx.getReg(this.dest).val % ctx.memory[fix(ctx.getReg(this.src).val) / 2]);
					break;
		default: throw new RuntimeException("Unsupported operation type: " + type);
		}
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.memory[fix(ctx.getReg(this.src).val) / 2], ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
