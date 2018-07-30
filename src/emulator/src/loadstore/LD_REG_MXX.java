package emulator.src.loadstore;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class LD_REG_MXX extends Instruction {
	public LD_REG_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("ld " + this.sdest + ", [0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.getReg(this.dest).val = ctx.memory[fix(this.argument) / 2];
		ctx.pc.val += 4;
	}
}
