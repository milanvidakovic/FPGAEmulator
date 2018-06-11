package emulator.src.pushpop;

import emulator.engine.Context;
import emulator.src.Instruction;

public class PUSH_REG extends Instruction {
	public PUSH_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("push " + this.sdest);
	}
	
	@Override
	public void exec(Context ctx) {
		int v = fix(ctx.sp.val);
		ctx.memory[v] = ctx.getReg(this.dest).val;
		ctx.sp.val++;
		ctx.pc.val++;
		updateViewer(ctx, v, ctx.getReg(this.dest).val);
	}
}
