package emulator.src.nopmovinpushrethalt;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class RET extends Instruction {
	public RET(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setAssembler("ret");
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.sp.val -= 2;
		ctx.pc.val = ctx.memory[fix(ctx.sp.val) / 2];
	}
}
