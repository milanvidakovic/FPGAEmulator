package emulator.src.andorxor;

import emulator.engine.Context;
import emulator.src.Instruction;

public class OR_REGX_REGY extends Instruction {
	public OR_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("or " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val | ctx.getReg(this.src).val;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.getReg(this.src).val, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
