package emulator.engine;

import javax.swing.SwingWorker;

import emulator.EmulatorMain;
import emulator.framebuffer.FBViewer;
import emulator.memviewer.MemViewer;
import emulator.src.Instruction;
import emulator.src.NotImplementedException;

/**
 * Execution engine. Capable of running in full speed, stepping over and
 * stepping into. supports breakpoints.
 */
public class Engine {
	public CpuContext ctx;
	public EmulatorMain main;
	public SwingWorker<Void, Instruction> worker;
	public boolean running = false;
	private MemViewer memViewer;
	private MemViewer sfViewer;
	private FBViewer fbViewer;
	private boolean halted;

	public static boolean inIrq = false; 
	public static short IRQ1_ADDR = 16;
	public static boolean irq1 = false;
	public static short IRQ2_PRESSED_ADDR = 32;
	public static short IRQ2_RELEASED_ADDR = 40;
	public static boolean irq2_pressed = false, irq2_released = false;

	public static int VIDEO_OFFS = 26880;

	public Engine(CpuContext ctx, EmulatorMain main) {
		this.ctx = ctx;
		this.main = main;
		this.worker = null;
		this.ctx.reset();
		this.ctx.engine = this;
		halted = false;
	}

	public void reset() {
		this.stop();
		this.ctx.reset();
		this.fbViewer.reset();
		running = false;
		halted = false;
		refreshUI(null);
		main.tblSrc.setRowSelectionInterval(0, 0);
		main.tblSrc.scrollRectToVisible(main.tblSrc.getCellRect(0, 0, true));
	}

	public void stop() {
		if (this.worker != null) {
			running = false;
			this.worker.cancel(true);
			this.worker = null;
		}
		if (this.fromStepOver) {
			this.fromStepOver = false;
			Instruction i = ctx.mdl.addr_instr[Instruction.fix(ctx.pc.val)];
			if (i.breakPoint) {
				i.breakPoint = false;
				this.ctx.mdl.fireTableDataChanged();
			}

		}
	}

	public void run() {
		if (halted) {
			return;
		}
		running = true;
		worker = new SwingWorker<Void, Instruction>() {
			// executed on a background worker thread
			@Override
			protected Void doInBackground() throws Exception {
				while (running) {
					if (halted) {
						break;
					}
					if (!inIrq && (Engine.irq1 || Engine.irq2_pressed || Engine.irq2_released)) {
						inIrq = true;
						prepareIrq();
					}
					Instruction i = ctx.mdl.addr_instr[Instruction.fix(ctx.pc.val)];
					if (i.breakPoint) {
						stop();
						break;
					}
					try {
						i.exec(ctx);
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new Exception(ex.getMessage());
					}
					if (EmulatorMain.DEBUG)
						publish(i);
				}
				return null;
			}

			@Override
			protected void process(java.util.List<Instruction> chunks) {
				refreshUI(chunks.get(0));
			}

			@Override
			protected void done() {
				main.btnRun.setEnabled(true);
				running = false;
			}
		};

		worker.execute();
	}

	private void prepareIrq() {
		// Push flags
		ctx.memory[Instruction.fix(ctx.sp.val) / 2] = ctx.f.val;
		ctx.sp.val += 2;
		// Push PC
		ctx.memory[Instruction.fix(ctx.sp.val) / 2] = ctx.pc.val;
		ctx.sp.val += 2;
		if (Engine.irq1) {
			// Jump to the IRQ1 handler
			ctx.pc.val = Engine.IRQ1_ADDR;
			Instruction instr = ctx.mdl.getInstruction(ctx.memory, Engine.IRQ1_ADDR);
			instr.setContent();
			ctx.mdl.lines.set(Engine.IRQ1_ADDR, instr);
			instr.tableLine = Engine.IRQ1_ADDR;
			ctx.mdl.addr_instr[Engine.IRQ1_ADDR] = instr;
			Engine.irq1 = false;
		} else if (Engine.irq2_pressed) {
			// Jump to the IRQ2 pressed handler
			ctx.pc.val = Engine.IRQ2_PRESSED_ADDR;
			Instruction instr = ctx.mdl.getInstruction(ctx.memory, Engine.IRQ2_PRESSED_ADDR);
			instr.setContent();
			ctx.mdl.lines.set(Engine.IRQ2_PRESSED_ADDR, instr);
			instr.tableLine = Engine.IRQ2_PRESSED_ADDR;
			ctx.mdl.addr_instr[Engine.IRQ2_PRESSED_ADDR] = instr;
			Engine.irq2_pressed = false;
		} else if (Engine.irq2_released) {
			// Jump to the IRQ2 released handler
			ctx.pc.val = Engine.IRQ2_RELEASED_ADDR;
			Instruction instr = ctx.mdl.getInstruction(ctx.memory, Engine.IRQ2_RELEASED_ADDR);
			instr.setContent();
			ctx.mdl.lines.set(Engine.IRQ2_RELEASED_ADDR, instr);
			instr.tableLine = Engine.IRQ2_RELEASED_ADDR;
			ctx.mdl.addr_instr[Engine.IRQ2_RELEASED_ADDR] = instr;
			Engine.irq2_released = false;
		}
	}

	public void stepInto() throws NotImplementedException {
		if (!halted) {
			if (Engine.irq1) {
				prepareIrq();
			}
			Instruction i = ctx.mdl.addr_instr[Instruction.fix(ctx.pc.val)];
			i.exec(ctx);
			refreshUI(i);
		}
	}

	boolean fromStepOver = false;

	public void stepOver() throws NotImplementedException {
		if (!halted) {
			this.fromStepOver = false;
			Instruction i = ctx.mdl.addr_instr[Instruction.fix(ctx.pc.val)];
			if (i != null && isCall(i)) {
				Instruction next = ctx.mdl.lines.get(i.tableLine + 1);
				if (next != null) {
					next.breakPoint = true;
					this.fromStepOver = true;
					run();
				}
			} else if (i != null) {
				this.fromStepOver = true;
				stepInto();
			}
		}
	}

	private boolean isCall(Instruction i) {
		switch (i.opcode & 0x000f) {
		case 0x2:
			return true;
		}
		return false;
	}

	public void refreshUI(Instruction i) {
		ctx.getReg(0).update();
		ctx.getReg(1).update();
		ctx.getReg(2).update();
		ctx.getReg(3).update();
		ctx.getReg(4).update();
		ctx.getReg(5).update();
		ctx.getReg(6).update();
		ctx.getReg(7).update();
		ctx.pc.update();
		ctx.sp.update();
		ctx.h.update();
		ctx.f.update();
		if (i != null && ctx.pc.val < ctx.mdl.addr_instr.length) {
			i = ctx.mdl.addr_instr[Instruction.fix(ctx.pc.val)];
			main.tblSrc.setRowSelectionInterval(i.tableLine, i.tableLine);
			if (main.tblSrc.getCellRect(i.tableLine, 0, true).y > main.src.getViewport().getHeight()) {
				main.tblSrc.scrollRectToVisible(main.tblSrc.getCellRect(i.tableLine, 0, true));
			} else {
				main.tblSrc.scrollRectToVisible(main.tblSrc.getCellRect(0, 0, true));
			}
		}
	}

	public void setMemViewer(MemViewer memViewer) {
		this.memViewer = memViewer;
	}

	public void setSfViewer(MemViewer sfViewer) {
		this.sfViewer = sfViewer;
	}

	public void setFBViewer(FBViewer fbViewer) {
		this.fbViewer = fbViewer;
	}

	public void updateViewer(int addr, short content) {
		fbViewer.updateCell(addr, content);
		if (EmulatorMain.DEBUG) {
			memViewer.updateCell(addr, content);
			sfViewer.updateCell(addr, content);

			sfViewer.tblMem.setRowSelectionInterval(addr / 8, addr / 8);
			sfViewer.tblMem.setColumnSelectionInterval(((addr / 2) % 4) / 2 + 1, ((addr / 2) % 4) / 2 + 1);
			sfViewer.tblMem.scrollRectToVisible(sfViewer.tblMem.getCellRect(addr / 8, 0, true));
			sfViewer.src.revalidate();
			sfViewer.src.repaint();
		}
	}

	public void halt() {
		halted = true;
	}

}