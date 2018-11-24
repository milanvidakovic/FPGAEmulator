package emulator.src.cmpneg;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class INV_REG extends Instruction {
	public INV_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("inv " + this.sdest);
	}
	
	@Override
	public void exec(CpuContext ctx) {
		int res = ~ctx.getReg(this.dest).val;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
