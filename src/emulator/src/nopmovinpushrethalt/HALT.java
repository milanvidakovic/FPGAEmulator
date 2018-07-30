package emulator.src.nopmovinpushrethalt;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class HALT extends Instruction {
	public HALT(short[]memory, int addr) {
		super(memory, addr, 0, 0);
		this.assembler = "halt";
		super.setContent();
	}
	
	@Override
	public void exec(CpuContext ctx) {
		//ctx.engine.halt();
		//ctx.pc.val++;
	}
}
