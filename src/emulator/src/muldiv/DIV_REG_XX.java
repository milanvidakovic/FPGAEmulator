package emulator.src.muldiv;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DIV_REG_XX extends Instruction {
	public DIV_REG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("div " + this.sdest + ", 0x%04x");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val / this.argument;
		int rem = ctx.getReg(this.dest).val % this.argument;
		ctx.getReg(this.dest).val = (short)res;
		ctx.h.val = (short)rem;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
