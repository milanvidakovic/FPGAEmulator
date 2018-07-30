package emulator.src.loadstore;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class LD_B_REG_MXX extends Instruction {
	public LD_B_REG_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("ld.b " + this.sdest + ", [0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int fixedAddr = fix(this.argument);
		if ((fixedAddr & 1) == 0)
			ctx.getReg(this.dest).val = (short)(ctx.memory[fixedAddr / 2] >> 8);
		else
			ctx.getReg(this.dest).val = (short)(ctx.memory[fixedAddr / 2] & 255);
		ctx.pc.val += 2;
	}
}
