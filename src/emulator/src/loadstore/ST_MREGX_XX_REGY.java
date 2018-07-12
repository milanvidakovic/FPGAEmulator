package emulator.src.loadstore;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ST_MREGX_XX_REGY extends Instruction {
	public ST_MREGX_XX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("st [" + this.sdest + " + 0x%04x], " + this.ssrc);
	}
	@Override
	public void exec(Context ctx) {
		ctx.memory[fix(ctx.getReg(this.dest).val + this.argument) / 2] = ctx.getReg(this.src).val;
		ctx.pc.val += 4;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val + this.argument), ctx.getReg(this.src).val);
	}
}
