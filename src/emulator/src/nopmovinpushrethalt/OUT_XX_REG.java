package emulator.src.nopmovinpushrethalt;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class OUT_XX_REG extends Instruction {
	public OUT_XX_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("out [0x%04x], " + this.ssrc);
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.toPort(this.argument, ctx.getReg(this.src).val);
		ctx.pc.val += 4;
	}
}
