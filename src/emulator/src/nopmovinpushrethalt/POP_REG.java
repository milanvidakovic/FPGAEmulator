package emulator.src.nopmovinpushrethalt;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class POP_REG extends Instruction {
	public POP_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("pop " + this.sdest);
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.sp.val -= 2;
		int v = fix(ctx.sp.val);
		ctx.getReg(this.dest).val = ctx.memory[v / 2];
		ctx.pc.val += 2;
	}
}
