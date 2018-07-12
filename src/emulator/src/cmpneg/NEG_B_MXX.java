package emulator.src.cmpneg;

import emulator.engine.Context;
import emulator.src.Instruction;

public class NEG_B_MXX extends Instruction {
	public NEG_B_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("neg.b [0x%04x]");
	}

	@Override
	public void exec(Context ctx) {
		int fixedAddr = fix(this.argument);
		short operand;
		if ((fixedAddr & 1) == 0)
			operand = (short)(ctx.memory[fixedAddr / 2] >> 8);
		else
			operand = (short)(ctx.memory[fixedAddr / 2] & 255);		
		
		int res = ~operand;

		short content = ctx.memory[fixedAddr / 2];
		if ((fixedAddr & 1) == 0) {
			content &= 0x00ff; 
			content |= res << 8;
		} else {
			content &= 0xff00; 
			content |= res & 255;
		}

		ctx.memory[fixedAddr / 2] = content;

		markFlags(res, (short)res, ctx);
		ctx.pc.val += 4;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}