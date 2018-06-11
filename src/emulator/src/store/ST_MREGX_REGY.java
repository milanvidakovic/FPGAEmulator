package emulator.src.store;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ST_MREGX_REGY extends Instruction {
	public ST_MREGX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setAssembler("st [" + this.sdest + "], " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		ctx.memory[fix(ctx.getReg(this.dest).val)] = ctx.getReg(this.src).val;
		ctx.pc.val++;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val), ctx.getReg(this.src).val);
	}
}
