package emulator;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import emulator.engine.Context;
import emulator.engine.Engine;
import emulator.framebuffer.FBViewer;
import emulator.memviewer.MemViewer;
import emulator.src.Instruction;
import emulator.src.NotImplementedException;
import emulator.util.IniFile;

public class EmulatorMain extends JFrame {
	private static final long serialVersionUID = 5554754132655656443L;

	final JFileChooser fc = new JFileChooser();

	public JButton btnLoad = new JButton("Load");
	public JButton btnRun = new JButton("Run");
	public JButton btnStop = new JButton("Stop");
	public JButton btnStepInto = new JButton("Step into");
	public JButton btnStepOver = new JButton("Step over");
	public JButton btnReset = new JButton("Reset");
	public JButton btnExit = new JButton("Exit");

	public JScrollPane src;
	public JTable tblSrc;

	/**
	 * CPU context. Holds all registers, flags and memory.
	 */
	Context ctx = new Context();
	/**
	 * Execution engine. Capable of running in full speed,
	 * stepping over and stepping into. supports breakpoints.
	 */
	Engine eng;
	/**
	 * Memory viewer. Content updated when instruction
	 * writes something in the memory.
	 */
	public MemViewer memViewer;
	/**
	 * The same memory viewer, but focused on the stack. 
	 * Used to observe stack frame.
	 * Content updated when instruction
	 * writes something in the memory.
	 */
	public MemViewer sfViewer;
	
	public FBViewer fbViewer;
	
	public MouseListener popupListener; 

	/**
	 * Ini file wrapper. Configuration written and read from the ini file.
	 */
	public IniFile ini;

	public EmulatorMain() {
		ini = new IniFile("emulator.ini");

		JPanel registers = new JPanel();
		registers.setLayout(new GridLayout(2, 4));

		registers.add(ctx.getReg(0));
		registers.add(ctx.getReg(1));
		registers.add(ctx.getReg(2));
		registers.add(ctx.getReg(3));
		registers.add(ctx.getReg(4));
		registers.add(ctx.getReg(5));
		registers.add(ctx.getReg(6));
		registers.add(ctx.getReg(7));
		registers.add(ctx.pc);
		registers.add(ctx.sp);
		registers.add(ctx.h);
		registers.add(ctx.f);

		getContentPane().add(registers, BorderLayout.NORTH);

		tblSrc = new JTable(ctx.mdl);
		src = new JScrollPane(tblSrc);
		getContentPane().add(src, BorderLayout.CENTER);
		
		// popup menu
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Go to address");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If we choose the "Go to address" option from the 
				// popup menu over some instruction
				// this code will just go to that location 
				// (won't set the break point) 
				Instruction i = ctx.mdl.lines.get(tblSrc.getSelectedRow());
				if (i != null) {
					// get the argument of that instruction
					// and look at it as an address
					short addr = i.argument;
					// try to find an instruction at that address
					i = ctx.mdl.addr_instr[addr];
					if (i != null) {
						// if there is indeed an instruction
						// obtain the row in the source table 
						int row = i.tableLine;
						// select that row
						tblSrc.setRowSelectionInterval(row, row);
						tblSrc.scrollRectToVisible(tblSrc.getCellRect(row, 0, true));
					}
				}
			}
		});
		popup.add(menuItem);
		popupListener = new PopupListener(popup);

		JPanel commands = new JPanel();
		commands.add(btnLoad);
		btnLoad.addActionListener(e -> loadProg());
		commands.add(Box.createHorizontalStrut(50));
		commands.add(btnRun);
		btnRun.setToolTipText("F8");
		btnRun.setEnabled(false);
		btnRun.addActionListener(e -> eng.run());
		commands.add(btnStop);
		btnStop.addActionListener(e -> eng.stop());
		btnStop.setToolTipText("ESC");
		btnStop.setEnabled(false);
		commands.add(Box.createHorizontalStrut(50));

		commands.add(btnStepOver);
		btnStepOver.setEnabled(false);
		btnStepOver.setToolTipText("F10");
		btnStepOver.addActionListener(e -> {
			try {
				eng.stepOver();
			} catch (NotImplementedException e1) {
				e1.printStackTrace();
			}
		});
		commands.add(btnStepInto);
		btnStepInto.setEnabled(false);
		btnStepInto.setToolTipText("F11");
		btnStepInto.addActionListener(e -> {
			try {
				eng.stepInto();
			} catch (NotImplementedException e1) {
				e1.printStackTrace();
			}
		});

		commands.add(Box.createHorizontalStrut(50));
		commands.add(btnReset);
		btnReset.addActionListener(e -> {
			eng.reset();
		});
		btnReset.setToolTipText("R");
		btnReset.setEnabled(false);
		commands.add(Box.createHorizontalStrut(100));
		commands.add(btnExit);
		btnExit.addActionListener(e -> {
			saveWindows();
			System.exit(0);
		});
		getContentPane().add(commands, BorderLayout.SOUTH);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_RELEASED) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_F8:
						btnRun.doClick();
						e.consume();
						break;
					case KeyEvent.VK_F10:
						btnStepOver.doClick();
						e.consume();
						break;
					case KeyEvent.VK_F11:
						btnStepInto.doClick();
						e.consume();
						break;
					case KeyEvent.VK_ESCAPE:
						btnStop.doClick();
						e.consume();
						break;
					case KeyEvent.VK_R:
						btnReset.doClick();
						e.consume();
						break;
					}
				}
				return false;
			}
		});

		setSize(new Dimension(ini.getInt("general", "width", 400), ini.getInt("general", "height", 700)));
		setLocation(ini.getInt("general", "x", 1024), ini.getInt("general", "y", 0));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void saveWindows() {
		if (memViewer != null) {
			ini.setInt("MemViewer", "width", memViewer.getWidth());
			ini.setInt("MemViewer", "height", memViewer.getHeight());
			ini.setInt("MemViewer", "x", memViewer.getX());
			ini.setInt("MemViewer", "y", memViewer.getY());
		}
		if (sfViewer != null) {
			ini.setInt("SfViewer", "width", sfViewer.getWidth());
			ini.setInt("SfViewer", "height", sfViewer.getHeight());
			ini.setInt("SfViewer", "x", sfViewer.getX());
			ini.setInt("SfViewer", "y", sfViewer.getY());
		}
		if (fbViewer != null) {
			ini.setInt("FB", "width", fbViewer.getWidth());
			ini.setInt("FB", "height", fbViewer.getHeight());
			ini.setInt("FB", "x", fbViewer.getX());
			ini.setInt("FB", "y", fbViewer.getY());
		}
		ini.setInt("general", "width", getWidth());
		ini.setInt("general", "height", getHeight());
		ini.setInt("general", "x", getX());
		ini.setInt("general", "y", getY());
		ini.saveINI();
	}

	/**
	 * Loading machine code into the memory.
	 */
	private void loadProg() {
		fc.setCurrentDirectory(new File(ini.getString("general", "startDir", ".")));
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (eng != null) eng.halt();
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			File file = fc.getSelectedFile();
			ctx.load(file.getAbsolutePath());
			setTitle(file.getName());
			ini.setString("general", "startDir", file.getAbsolutePath());
			ini.saveINI();
			eng = new Engine(ctx, this);
			if (memViewer != null) {
				memViewer.dispose();
			}
			memViewer = new MemViewer(ctx, eng, "MemViewer");

			if (sfViewer != null) {
				sfViewer.dispose();
			}
			sfViewer = new MemViewer(ctx, eng, "SfViewer");

			if (fbViewer != null) {
				fbViewer.dispose();
			}
			fbViewer = new FBViewer(ctx, eng);

			eng.setMemViewer(memViewer);
			eng.setSfViewer(sfViewer);
			eng.setFBViewer(fbViewer);

			src.remove(tblSrc);
			tblSrc = new JTable(ctx.mdl);
			src.getViewport().add(tblSrc);
			src.revalidate();
			src.repaint();
			tblSrc.setRowSelectionInterval(0, 0);
			tblSrc.addMouseListener(popupListener);

			btnRun.setEnabled(true);
			btnStepOver.setEnabled(true);
			btnStepInto.setEnabled(true);
			btnReset.setEnabled(true);
			btnStop.setEnabled(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public static void main(String[] args) {
		new EmulatorMain();

	}

}
