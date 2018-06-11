package emulator.src.incdecneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class INC_MXX extends Instruction {
	public INC_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("inc [0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ctx.memory[fix(this.argument)] + 1;
		ctx.memory[fix(this.argument)] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val+=2;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}
