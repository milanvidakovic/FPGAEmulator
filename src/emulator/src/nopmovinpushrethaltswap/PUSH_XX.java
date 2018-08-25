package emulator.src.nopmovinpushrethaltswap;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class PUSH_XX extends Instruction {
	public PUSH_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("push 0x%04x");
	}

	@Override
	public void exec(CpuContext ctx) {
		int v = fix(ctx.sp.val);
		ctx.memory[v / 2] = this.argument;
		ctx.sp.val += 2;
		ctx.pc.val += 4;
		updateViewer(ctx, v, this.argument);
	}
}
