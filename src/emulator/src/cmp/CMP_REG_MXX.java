package emulator.src.cmp;

import emulator.engine.Context;
import emulator.src.Instruction;

public class CMP_REG_MXX extends Instruction {
	public CMP_REG_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("cmp" + this.sdest + ", [0x%04x]");
	}
	
	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val - ctx.memory[fix(this.argument)];
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.memory[fix(this.argument)], ctx.getReg(this.dest).val, ctx);
		ctx.pc.val +=2;
	}
}
