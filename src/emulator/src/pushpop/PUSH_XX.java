package emulator.src.pushpop;

import emulator.engine.Context;
import emulator.src.Instruction;

public class PUSH_XX extends Instruction {
	public PUSH_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("push 0x%04x");
	}

	@Override
	public void exec(Context ctx) {
		int v = fix(ctx.sp.val);
		ctx.memory[v] = this.argument;
		ctx.sp.val++;
		ctx.pc.val+=2;
		updateViewer(ctx, v, this.argument);
	}
}
