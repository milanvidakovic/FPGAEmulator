package emulator.src.incdecneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class NEG_REG extends Instruction {
	public NEG_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("neg " + this.sdest);
	}
	
	@Override
	public void exec(Context ctx) {
		int res = ~ctx.getReg(this.dest).val;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val++;
	}
}
