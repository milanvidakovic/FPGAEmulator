package emulator.src.loadstore;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class LD_B_REGX_MREGY_XX extends Instruction {
	public LD_B_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("ld " + this.sdest + ", [" + this.ssrc + " + 0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int fixedAddr = fix(ctx.getReg(this.src).val + this.argument);
		if ((fixedAddr & 1) == 0)
			ctx.getReg(this.dest).val = (short) (ctx.memory[fixedAddr / 2] >> 8);
		else
			ctx.getReg(this.dest).val = (short) (ctx.memory[fixedAddr / 2] & 255);
		ctx.pc.val += 4;
	}
}
