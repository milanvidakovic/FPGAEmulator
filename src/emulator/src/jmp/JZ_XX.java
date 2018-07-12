package emulator.src.jmp;

import emulator.engine.Context;
import emulator.src.Instruction;

public class JZ_XX extends Instruction {
	public JZ_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("jz 0x%04x");
		super.isJump = true;
	}

	@Override
	public void exec(Context ctx) {
		if ((ctx.f.val & 1) == 1) {
			ctx.pc.val = this.argument;
		} else {
			ctx.pc.val += 4;
		}
	}
}
