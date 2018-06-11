package emulator.src.load;

import emulator.engine.Context;
import emulator.src.Instruction;

public class LD_REGX_MREGY extends Instruction {
	public LD_REGX_MREGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("ld " + this.sdest + ", [" + this.ssrc + "]");
	}

	@Override
	public void exec(Context ctx) {
		ctx.getReg(this.dest).val = ctx.memory[fix(ctx.getReg(this.src).val)];
		ctx.pc.val++;
	}
}
