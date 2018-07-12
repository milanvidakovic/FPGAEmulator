package emulator.src.incdec;

import emulator.engine.Context;
import emulator.src.Instruction;

public class DEC_REG extends Instruction {
	public DEC_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("dec " + this.sdest);
	}
	
	@Override
	public void exec(Context ctx) {
		int res = ctx.getReg(this.dest).val - 1;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
