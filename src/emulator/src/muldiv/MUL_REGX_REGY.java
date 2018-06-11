package emulator.src.muldiv;

import emulator.engine.Context;
import emulator.src.Instruction;

public class MUL_REGX_REGY extends Instruction {
	public MUL_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("mul " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val * ctx.getReg(this.src).val;
		ctx.getReg(this.dest).val = (short)(res & 0xffff);
		ctx.h.val = (short)((res & 0xffff0000) >> 16);
		markFlags((short)res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
