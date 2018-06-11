package emulator.src.addsub;

import emulator.engine.Context;
import emulator.src.Instruction;

public class SUB_REG_XX extends Instruction {
	public SUB_REG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("sub " + this.sdest + ", 0x%04x");
	}

	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - this.argument;
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, this.argument, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val+=2;
	}
}
