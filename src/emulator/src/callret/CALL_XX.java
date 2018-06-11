package emulator.src.callret;

import emulator.engine.Context;
import emulator.src.Instruction;

public class CALL_XX extends Instruction {
	public CALL_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("call 0x%04x");
		super.isJump = true;
	}
	
	@Override
	public void exec(Context ctx) {
		ctx.memory[fix(ctx.sp.val)] = (short)(ctx.pc.val + 2);
		updateViewer(ctx, fix(ctx.sp.val), (short)(ctx.pc.val + 2));
		ctx.sp.val++;
		ctx.pc.val = this.argument;
	}
}
