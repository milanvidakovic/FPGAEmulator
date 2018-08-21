package emulator.memviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import emulator.EmulatorMain;
import emulator.engine.CpuContext;
import emulator.engine.Engine;

/**
 * Memory viewer. Content updated when instruction
 * writes something in the memory.
 */
public class MemViewer extends JFrame {
	private static final long serialVersionUID = -5500314457803056242L;
	public JTable tblMem;
	public MemModel memMdl;
	public JScrollPane src;

	public JLabel display = new JLabel();

	public MemViewer(CpuContext ctx, Engine eng, String catName) {
		super();
		if (catName.equals("MemViewer")) {
			setTitle("Memory");
		} else {
			setTitle("Memory write");
		}
		memMdl = new MemModel(ctx);
		tblMem = new JTable(memMdl);
		src = new JScrollPane(tblMem);
		getContentPane().add(src, BorderLayout.CENTER);

		JPanel pDisp = new JPanel();
		display.setFont(display.getFont().deriveFont(16f));
		display.setText("                ");
		pDisp.add(display);
		getContentPane().add(pDisp, BorderLayout.SOUTH);

		setSize(new Dimension(ctx.engine.main.ini.getInt(catName, "width", 400),
				ctx.engine.main.ini.getInt(catName, "height", 700)));
		setLocation(ctx.engine.main.ini.getInt(catName, "x", 1024),
				ctx.engine.main.ini.getInt(catName, "y", 0));

		setVisible(true);
	}

	public void updateCell(int addr, short content) {
		int row = addr / 8;
		int col = (addr/2) % 4;
		if (EmulatorMain.DEBUG)
			System.out.println("CHANGED MEMORY LOCATION CONTENT at addr: " + String.format("0x%04x", addr) + ", to: " + String.format("0x%04x", content));
		memMdl.setValueAt(content, row, col);
	}

}
