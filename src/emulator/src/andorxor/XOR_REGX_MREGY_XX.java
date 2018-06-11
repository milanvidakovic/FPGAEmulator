package emulator.src.andorxor;

import emulator.engine.Context;
import emulator.src.Instruction;

public class XOR_REGX_MREGY_XX extends Instruction {
	public XOR_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("xor " + this.sdest + ", [" + this.ssrc + " + 0x%04x]");
	}
	
	@Override
	public void exec(Context ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = ctx.getReg(this.dest).val ^ ctx.memory[fix(ctx.getReg(this.src).val + this.argument)];
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, ctx.memory[fix(ctx.getReg(this.src).val + this.argument)], ctx.getReg(this.dest).val, ctx);
		ctx.pc.val+=2;
	}
}
