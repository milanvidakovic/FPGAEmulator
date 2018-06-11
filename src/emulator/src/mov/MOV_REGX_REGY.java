package emulator.src.mov;

import emulator.engine.Context;
import emulator.src.Instruction;

public class MOV_REGX_REGY extends Instruction {
	public MOV_REGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("mov " + this.sdest + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		ctx.getReg(this.dest).val = ctx.getReg(this.src).val;
		ctx.pc.val++;
	}
}
