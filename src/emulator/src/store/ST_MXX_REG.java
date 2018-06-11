package emulator.src.store;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ST_MXX_REG extends Instruction {
	public ST_MXX_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("st [0x%04x]" + ", " + this.ssrc);
	}

	@Override
	public void exec(Context ctx) {
		ctx.memory[fix(this.argument)] = ctx.getReg(this.dest).val;
		ctx.pc.val += 2;
		updateViewer(ctx, fix(this.argument), ctx.getReg(this.dest).val);
	}
}
