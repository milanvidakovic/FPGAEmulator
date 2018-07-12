package emulator.src.nopmovinpushrethalt;

import emulator.engine.Context;
import emulator.src.Instruction;

public class HALT extends Instruction {
	public HALT(short[]memory, int addr) {
		super(memory, addr, 0, 0);
		this.assembler = "halt";
		super.setContent();
	}
	
	@Override
	public void exec(Context ctx) {
		//ctx.engine.halt();
		//ctx.pc.val++;
	}
}
