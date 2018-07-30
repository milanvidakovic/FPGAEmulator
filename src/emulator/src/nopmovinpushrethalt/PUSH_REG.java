package emulator.src.nopmovinpushrethalt;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class PUSH_REG extends Instruction {
	public PUSH_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("push " + this.sdest);
	}
	
	@Override
	public void exec(CpuContext ctx) {
		int v = fix(ctx.sp.val);
		ctx.memory[v / 2] = ctx.getReg(this.dest).val;
		ctx.sp.val += 2;
		ctx.pc.val += 2;
		updateViewer(ctx, v, ctx.getReg(this.dest).val);
	}
}
