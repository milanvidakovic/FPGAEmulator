package emulator.src.muldiv;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DIV_REGX_REGY extends Instruction {
	public DIV_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("div " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val / ctx.getReg(this.src).val;
		int rem = ctx.getReg(this.dest).val % ctx.getReg(this.src).val;
		ctx.getReg(this.dest).val = (short)res;
		ctx.h.val = (short)rem;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
