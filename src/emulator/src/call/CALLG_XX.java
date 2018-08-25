package emulator.src.call;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class CALLG_XX extends Instruction {
	public CALLG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("callp 0x%04x");
		super.isJump = true;
	}

	@Override
	public void exec(CpuContext ctx) {
		if (((ctx.f.val & 0x8) == 1) && ((ctx.f.val & 0x1) == 0)) {
			ctx.memory[fix(ctx.sp.val)] = (short)(ctx.pc.val + 4);
			updateViewer(ctx, fix(ctx.sp.val), (short)(ctx.pc.val + 4));
			ctx.sp.val += 2;
			ctx.pc.val = this.argument;
		}
	}
}
