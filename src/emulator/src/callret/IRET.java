package emulator.src.callret;

import emulator.engine.Context;
import emulator.engine.Engine;
import emulator.src.Instruction;

public class IRET extends Instruction {
	public IRET(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setAssembler("iret");
	}

	@Override
	public void exec(Context ctx) {
		ctx.sp.val--;
		ctx.pc.val = ctx.memory[fix(ctx.sp.val)];
		ctx.sp.val--;
		ctx.f.val = ctx.memory[fix(ctx.sp.val)];
		Engine.irq1 = false;
	}
}
