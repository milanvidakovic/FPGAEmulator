package emulator.src.shift;

import emulator.engine.Context;
import emulator.src.Instruction;

public class SHL_REGX_MREGY_XX extends Instruction {
	public SHL_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("shl " + this.sdest + ", [" + this.ssrc +" + 0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val << ctx.memory[fix(ctx.getReg(this.src).val + this.argument)];
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val +=2;
		//updateViewer(ctx, fix(ctx.b.val + this.argument), ctx.a.val);
	}
}
