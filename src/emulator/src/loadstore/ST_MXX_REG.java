package emulator.src.loadstore;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class ST_MXX_REG extends Instruction {
	public ST_MXX_REG(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("st [0x%04x]" + ", " + this.sdest);
	}

	@Override
	public void exec(CpuContext ctx) {
		ctx.memory[fix(this.argument) / 2] = ctx.getReg(this.dest).val;
		ctx.pc.val += 4;
		updateViewer(ctx, fix(this.argument), ctx.getReg(this.dest).val);
	}
}
