package emulator.src.incdec;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class DEC_MREG_XX extends Instruction {
	public DEC_MREG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("dec [" + this.sdest + " + 0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int res = ctx.memory[fix(ctx.getReg(this.dest).val + this.argument) / 2] - 1;
		ctx.memory[fix(ctx.getReg(this.dest).val + this.argument) / 2] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val += 4;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val + this.argument), (short)res);
	}
}
