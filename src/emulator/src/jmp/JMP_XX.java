package emulator.src.jmp;

import emulator.engine.Context;
import emulator.src.Instruction;

public class JMP_XX extends Instruction {
	public JMP_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("j 0x%04x");
		super.isJump = true;
	}

	@Override
	public void exec(Context ctx) {
		ctx.pc.val = this.argument;
	}
}
