package emulator.src.jmp;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class JMP_XX extends Instruction {
	public JMP_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, 0, 0);
		super.setArgument();
		super.setAssembler("j 0x%04x");
		super.isJump = true;
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.pc.val = this.argument;
	}
}
