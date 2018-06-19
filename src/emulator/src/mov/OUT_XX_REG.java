package emulator.src.mov;

import emulator.engine.Context;
import emulator.src.Instruction;

public class OUT_XX_REG extends Instruction {
	public OUT_XX_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("out [0x%04x], " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		// TODO: implement OUT
		ctx.pc.val += 2;
	}
}
