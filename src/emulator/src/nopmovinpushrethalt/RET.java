package emulator.src.nopmovinpushrethalt;

import emulator.engine.Context;
import emulator.src.Instruction;

public class RET extends Instruction {
	public RET(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setAssembler("ret");
	}

	@Override
	public void exec(Context ctx) {
		ctx.sp.val -= 2;
		ctx.pc.val = ctx.memory[fix(ctx.sp.val) / 2];
	}
}
