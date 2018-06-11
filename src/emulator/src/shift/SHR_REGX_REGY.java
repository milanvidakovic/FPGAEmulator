package emulator.src.shift;

import emulator.engine.Context;
import emulator.src.Instruction;

public class SHR_REGX_REGY extends Instruction {
	public SHR_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("shr " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val >> ctx.getReg(this.src).val;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
