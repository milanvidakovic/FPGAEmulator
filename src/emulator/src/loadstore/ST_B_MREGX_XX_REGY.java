package emulator.src.loadstore;

import emulator.engine.Context;
import emulator.src.Instruction;

public class ST_B_MREGX_XX_REGY extends Instruction {
	public ST_B_MREGX_XX_REGY(short[] memory, int addr, int src, int dest) {
		super(memory, addr, src, dest);
		super.setArgument();
		super.setAssembler("st.b [" + this.sdest + " + 0x%04x], " + this.ssrc);
	}
	@Override
	public void exec(Context ctx) {
		
		int fixedAddr = fix(ctx.getReg(this.dest).val + this.argument);
		short content = ctx.memory[fixedAddr / 2];
		if ((fixedAddr & 1) == 0) {
			content &= 0x00ff; 
			content |= ctx.getReg(this.src).val << 8;
		} else {
			content &= 0xff00; 
			content |= ctx.getReg(this.src).val & 255;
		}
		ctx.memory[fixedAddr / 2] = content;
		
		ctx.pc.val += 4;
		updateViewer(ctx, fix(ctx.getReg(this.dest).val + this.argument), content);
	}
}
