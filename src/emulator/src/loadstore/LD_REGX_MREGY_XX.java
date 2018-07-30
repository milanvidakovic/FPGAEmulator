package emulator.src.loadstore;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class LD_REGX_MREGY_XX extends Instruction {
	public LD_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("ld " + this.sdest + ", [" + this.ssrc + " + 0x%04x]");
	}
	
	@Override
	public void exec(CpuContext ctx) {
		ctx.getReg(this.dest).val = ctx.memory[fix(ctx.getReg(this.src).val + this.argument) / 2];
		ctx.pc.val += 4;
	}
}
