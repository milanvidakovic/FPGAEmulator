package emulator.src.alu;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class NEG_MREG extends Instruction {
	public NEG_MREG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("neg [" + this.sdest + "]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int res = -ctx.memory[fix(ctx.getReg(this.dest).val) / 2];
		ctx.memory[fix(ctx.getReg(this.dest).val) / 2] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val += 2;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val), (short)res);
	}
}
