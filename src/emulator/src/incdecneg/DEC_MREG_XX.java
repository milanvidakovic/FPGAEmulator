package emulator.src.incdecneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DEC_MREG_XX extends Instruction {
	public DEC_MREG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("dec [" + this.sdest + " + 0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.memory[fix(ctx.getReg(this.dest).val + this.argument)] - 1;
		ctx.memory[fix(ctx.getReg(this.dest).val + this.argument)] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val+=2;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val + this.argument), (short)res);
	}
}
