package emulator.src.cmpneg;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class CMP_REG_XX extends Instruction {
	public CMP_REG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("cmp " + this.sdest + ", 0x%04x");
	}

	@Override
	public void exec(CpuContext ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - this.argument;
		markFlags(res,(short)res, ctx);
		markOverflow(old_a, this.argument, (short)res, ctx);
		ctx.pc.val += 4;
	}
}
