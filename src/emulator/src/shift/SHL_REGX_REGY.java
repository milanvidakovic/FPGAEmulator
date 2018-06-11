package emulator.src.shift;

import emulator.engine.Context;
import emulator.src.Instruction;

public class SHL_REGX_REGY extends Instruction {
	public SHL_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("shl " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val << ctx.getReg(this.src).val;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
