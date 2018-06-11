package emulator.src;

import emulator.engine.Context;

public class Instruction {

	public static final int BREAK_POINT = 0;
	public static final int ADDR = 1;
	public static final int CONTENT = 2;
	public static final int ASSEMBLER = 3;

	public boolean breakPoint;
	public int addr;
	public String content = "";
	public short opcode;
	public int src;
	public String ssrc;
	public int dest;
	public String sdest;
	public boolean hasArgument = false;
	public short argument;
	public String assembler = "";
	public boolean isJump = false;
	
	public short[] memory;
	/**
	 * Table row where this instruction is placed.
	 */
	public int tableLine;

	private Instruction(short[]memory, int addr) {
		this.addr = addr - 1;
		this.memory = memory;
		this.opcode = memory[this.addr];
		setContent();
	}

	public Instruction(short[]memory, int addr, int src, int dest) {
		this(memory, addr);
		this.src = src;
		this.dest = dest;
		this.ssrc = getName(src);
		this.sdest = getName(dest);
	}

	private String getName(int reg) {
		switch(reg) {
		case 0: return "r0";
		case 1: return "r1";
		case 2: return "r2";
		case 3: return "r3";
		case 4: return "r4";
		case 5: return "r5";
		case 6: return "r6";
		case 7: return "r7";
		case 8: return "sp";
		case 9: return "h";
		}
		return "";
	}

	public void exec(Context ctx) throws NotImplementedException {
		throw new NotImplementedException(this.assembler + " not implemented yet!");
	}
	
	public Object toCell(int col) {
		switch (col) {
		case BREAK_POINT:
			return breakPoint;
		case ADDR:
			return String.format("%04X", addr);
		case CONTENT:
			return content;
		case ASSEMBLER:
			return assembler;
		}
		return null;
	}

	public void setContent() {
		if (this.hasArgument) {
			this.content = String.format("%04x, %04x", this.opcode, this.argument);
		} else {
			this.content = String.format("%04x", this.opcode);
		}
	}

	public void setArgument() {
		this.argument = memory[this.addr + 1];
		this.hasArgument = true;
	}

	public void setAssembler(String format) {
		if (this.hasArgument) {
			// negativan broj kao argument
			if ((this.argument & 0x8000) != 0) {
				this.assembler = String.format(format + "      ; -%04x", this.argument, neg(this.argument));
			} else {
				this.assembler = String.format(format, this.argument);
			}
		} else {
			this.assembler = format;
		}
	}
	
	public static int fix(short w) {
		return w & 0x000000000000FFFF;
	}

	protected int fix(int w) {
		return w & 0x000000000000FFFF;
	}

	protected void markFlags(int res, short r, Context ctx) {
		// Z flag
		if (r == 0) {
			ctx.f.val |= 1;
		} else {
			ctx.f.val &= 0xfffe;
		}
		// P flag
		if ( (r < 0) || ((r & 0x8000) == 1)) {
			ctx.f.val &= 0xfff7;
		} else {
			ctx.f.val |= 0x8;
		}
		
		// C flag
		if ((res & 0x10000) != 0) {
			ctx.f.val |= 1;
		} else {
			ctx.f.val &= 0xd;
		}
	}
	
	protected void markOverflow(short a, short b, short res, Context ctx) {
		int sa = sign(a);
		int sb = sign(b);
		int sr = sign(res);
		if (sa == sb) {
			if (sa != sr) {
				ctx.f.val |= 0x4;
				return;
			}
		}		
		ctx.f.val &= 0xfffb;
	}
	
	private int sign(short a) {
		return a & 0x8000;
	}

	protected void updateViewer(Context ctx, int addr, short content) {
		ctx.engine.updateViewer(addr, content);
	}
	
	private int neg(int arg) {
		return (65536 - arg) & 0xffff;
	}
}
