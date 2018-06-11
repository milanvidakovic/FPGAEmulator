package emulator.src.addsub;

import emulator.engine.Context;
import emulator.src.Instruction;

public class SUB_REGX_MREGY extends Instruction {
	public SUB_REGX_MREGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("sub " + this.sdest + ", [" + this.ssrc + "]");
	}

	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - ctx.memory[fix(ctx.getReg(this.src).val)];
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.memory[fix(ctx.getReg(this.src).val)], ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}