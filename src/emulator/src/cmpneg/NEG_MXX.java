package emulator.src.cmpneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class NEG_MXX extends Instruction {
	public NEG_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("neg [0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int res = ~ctx.memory[fix(this.argument) / 2];
		ctx.memory[fix(this.argument) / 2] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val += 4;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}
