package emulator.src.call;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class CALL_XX extends Instruction {
	public CALL_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("call 0x%04x");
		super.isJump = true;
	}
	
	@Override
	public void exec(CpuContext ctx) {
		ctx.memory[fix(ctx.sp.val)/2] = (short)(ctx.pc.val + 4);
		updateViewer(ctx, fix(ctx.sp.val), (short)(ctx.pc.val + 4));
		ctx.sp.val += 2;
		ctx.pc.val = this.argument;
	}
}
