package emulator.src.cmpneg;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class INV_MXX extends Instruction {
	public INV_MXX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		this.setArgument();
		super.setAssembler("inv [0x%04x]");
	}

	@Override
	public void exec(CpuContext ctx) {
		int res = ~ctx.memory[fix(this.argument) / 2];
		ctx.memory[fix(this.argument) / 2] = (short)res;
		markFlags(res, (short)res, ctx);
		ctx.pc.val += 4;
		updateViewer(ctx, fix(this.argument), (short)res);
	}
}
