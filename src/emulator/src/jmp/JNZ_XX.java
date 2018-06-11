package emulator.src.jmp;

import emulator.engine.Context;
import emulator.src.Instruction;

public class JNZ_XX extends Instruction {
	public JNZ_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("jnz 0x%04x");
		super.isJump = true;
	}

	@Override
	public void exec(Context ctx) {
		if ((ctx.f.val & 0x1) == 0) {
			ctx.pc.val = this.argument;
		} else {
			ctx.pc.val +=2;
		}
	}
}