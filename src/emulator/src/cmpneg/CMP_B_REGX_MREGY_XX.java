package emulator.src.cmpneg;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class CMP_B_REGX_MREGY_XX extends Instruction {
	public CMP_B_REGX_MREGY_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("cmp.b " + this.sdest + ", [" + this.ssrc + " + 0x%04x]");
	}
	
	@Override
	public void exec(CpuContext ctx) {
		short old_a = ctx.getReg(this.dest).val;
		
		int fixedAddr = fix(ctx.getReg(this.src).val + this.argument);
		short operand;
		if ((fixedAddr & 1) == 0)
			operand = (short)(ctx.memory[fixedAddr / 2] >> 8);
		else
			operand = (short)(ctx.memory[fixedAddr / 2] & 255);		
		
		int res = ctx.getReg(this.dest).val  - operand;
		
		markFlags(res, (short)res, ctx);
		markOverflow(old_a, operand, (short)res, ctx);
		ctx.pc.val += 4;
	}
}
