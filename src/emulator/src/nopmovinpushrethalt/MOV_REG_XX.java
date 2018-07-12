package emulator.src.nopmovinpushrethalt;

import emulator.engine.Context;
import emulator.src.Instruction;

public class MOV_REG_XX extends Instruction {
	public MOV_REG_XX(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("mov " + this.sdest + ", 0x%04x");
	}

	@Override
	public void exec(Context ctx) {
		ctx.getReg(this.dest).val = this.argument;
		ctx.pc.val += 4;
	}
}
