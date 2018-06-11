package emulator.src.nophalt;

import emulator.engine.Context;
import emulator.src.Instruction;

public class NOP extends Instruction {
	public NOP(short[]memory, int addr) {
		super(memory, addr, 0, 0);
		this.assembler = "nop";
		super.setContent();
	}
	
	@Override
	public void exec(Context ctx) {
		ctx.pc.val++;
	}
}
