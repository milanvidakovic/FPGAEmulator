package emulator.src.muldiv;

import emulator.engine.Context;
import emulator.src.Instruction;

public class MUL_REGX_MREGY_XX extends Instruction {
	public MUL_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("mul " + this.sdest + ", [" + this.ssrc + " + 0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val * ctx.memory[fix(ctx.getReg(this.src).val + this.argument)]; 
		ctx.getReg(this.dest).val = (short)(res & 0xffff);
		ctx.h.val = (short)((res & 0xffff0000) >> 16);
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
