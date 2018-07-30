package emulator.src.incdec;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class DEC_MXX extends Instruction {
	public DEC_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("dec [0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int res = ctx.memory[fix(this.argument) / 2] - 1;
		ctx.memory[fix(this.argument) / 2] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val += 4;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}
