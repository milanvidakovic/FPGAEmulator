package emulator.src.incdecneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DEC_MXX extends Instruction {
	public DEC_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("dec [0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.memory[fix(this.argument)] - 1;
		ctx.memory[fix(this.argument)] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val+=2;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}
