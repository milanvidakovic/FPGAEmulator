package emulator.src.incdecneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DEC_MREG extends Instruction {
	public DEC_MREG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("dec [" + this.sdest + "]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.memory[fix(ctx.getReg(this.dest).val)] - 1;
		ctx.memory[fix(ctx.getReg(this.dest).val)] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val++;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val), (short)res);
	}
}
