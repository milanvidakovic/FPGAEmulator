package emulator.src.cmpneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class CMP_REGX_MREGY extends Instruction {
	public CMP_REGX_MREGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("cmp " + this.sdest + ", [" + this.ssrc + "]");
	}

	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - ctx.memory[fix(ctx.getReg(this.src).val) / 2];
		markFlags(res, (short)res, ctx);
		markOverflow(old_a, ctx.memory[fix(ctx.getReg(this.src).val) / 2], (short)res, ctx);
		ctx.pc.val += 2;
	}
}
