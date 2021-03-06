package emulator.src.cmpneg;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class CMP_REGX_REGY extends Instruction {
	public CMP_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("cmp " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(CpuContext ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - ctx.getReg(this.src).val;
		markFlags(res, (short)res, ctx);
		markOverflow(old_a, ctx.getReg(this.src).val, (short)res, ctx);
		ctx.pc.val += 2;
	}
}
