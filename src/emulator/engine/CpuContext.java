package emulator.engine;

import emulator.framebuffer.FBViewer;
import emulator.registers.Register;
import emulator.src.SrcModel;

/**
 * CPU context. Holds all registers, flags and memory.
 */
public class CpuContext {
	public Register r0 = new Register("r0", this);
	public Register r1 = new Register("r1", this);
	public Register r2 = new Register("r2", this);
	public Register r3 = new Register("r3", this);
	public Register r4 = new Register("r4", this);
	public Register r5 = new Register("r5", this);
	public Register r6 = new Register("r6", this);
	public Register r7 = new Register("r7", this);

	public Register pc = new Register("PC", this);
	public Register sp = new Register("SP", this);
	public Register h = new Register("H", this);
	public Register f = new Register("F", this);

	// received uart byte
	public byte uart;

	public short[] memory = new short[65536];

	public SrcModel mdl;
	public Engine engine;

	public CpuContext() {
		this.mdl = new SrcModel(this.memory);
	}

	public void reset() {
		r0.val = 0;
		r1.val = 0;
		r2.val = 0;
		r3.val = 0;
		r4.val = 0;
		r5.val = 0;
		r6.val = 0;
		r7.val = 0;
		pc.val = 0;
		sp.val = 0;
		h.val = 0;
		f.val = 0;
		/*
		 * for (int i = 0; i < this.memory.length; i++) { this.memory[i] = 0; }
		 */
		if (this.engine != null && this.engine.main.memViewer != null) {
			engine.main.memViewer.display.setText("                ");
			engine.main.sfViewer.display.setText("                ");
		}
	}

	public Register getReg(int reg) {
		switch (reg) {
		case 0:
			return r0;
		case 1:
			return r1;
		case 2:
			return r2;
		case 3:
			return r3;
		case 4:
			return r4;
		case 5:
			return r5;
		case 6:
			return r6;
		case 7:
			return r7;
		case 8:
			return sp;
		case 9:
			return h;
		}
		return null;
	}

	public void load(String fileName) {
		memory = new short[65536];
		this.mdl = new SrcModel(fileName, memory);
	}

	public short fromPort(short port) {
		switch (port) {
		case 64: {
			// UART byte
			return uart;
		}
		}
		return 0;
	}
	
	public void toPort(short port, int value) {
		switch (port) {
		case 128:
			if (value == 1)
				engine.main.fbViewer.setMode(FBViewer.GRAPHICS_MODE_320_240);
			else
				engine.main.fbViewer.setMode(FBViewer.TEXT_MODE);
			break;
		}
	}
}
