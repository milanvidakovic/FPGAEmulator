package emulator.src.alu;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class ALU_REG_XX extends Instruction {
	int type;

	public ALU_REG_XX(short[] memory, int addr, int src, int dest, int type) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler(Instruction.getTypeStr(type) + this.sdest + ", 0x%04x");
		this.type = type;
	}

	@Override
	public void exec(CpuContext ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = 0;
		switch (type) {
		case ADD: res = ctx.getReg(this.dest).val + this.argument; break;
		case SUB: res = ctx.getReg(this.dest).val - this.argument; break;
		case AND: res = ctx.getReg(this.dest).val & this.argument; break;
		case OR : res = ctx.getReg(this.dest).val | this.argument; break;
		case XOR: res = ctx.getReg(this.dest).val ^ this.argument; break;
		case SHL: res = ctx.getReg(this.dest).val << this.argument; break;
		case SHR: res = ctx.getReg(this.dest).val >> this.argument; break;
		case MUL:	res = ctx.getReg(this.dest).val * this.argument; 
					ctx.h.val = (short)((res & 0xffff0000) >> 16);
					break;
		case DIV: 	res = ctx.getReg(this.dest).val / this.argument; 
					ctx.h.val = (short)(ctx.getReg(this.dest).val % this.argument);
					break;
		default: throw new RuntimeException("Unsupported operation type: " + type);
		}
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, this.argument, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 4;
	}
}
