package emulator.src.nopmovinpushrethalt;

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
		ctx.sp.val -=2;
		ctx.pc.val = ctx.memory[fix(ctx.sp.val) / 2];
		ctx.sp.val -=2;
		ctx.f.val = ctx.memory[fix(ctx.sp.val) / 2];
		Engine.irq1 = false;
	}
}