package emulator.framebuffer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import emulator.engine.Context;
import emulator.engine.Engine;

public class FBViewer extends JFrame {
	private static final long serialVersionUID = -5500314457803056242L;
	public JTable tblMem;
	public FBModel memMdl;
	public JScrollPane src;

	public JLabel display = new JLabel();

	public FBViewer(Context ctx, Engine eng) {
		super();
		setTitle("Frame buffer");
		memMdl = new FBModel(ctx);
		tblMem = new JTable(memMdl);
		src = new JScrollPane(tblMem);
		getContentPane().add(src, BorderLayout.CENTER);

		setSize(new Dimension(ctx.engine.main.ini.getInt("FB", "width", 400),
				ctx.engine.main.ini.getInt("FB", "height", 700)));
		setLocation(ctx.engine.main.ini.getInt("FB", "x", 1024), ctx.engine.main.ini.getInt("fb", "y", 0));

		setVisible(true);
	}

	public void updateCell(int addr, short content) {
		if (addr >= 2400 && addr <= (2400 + 80 * 60)) {
			int row = (addr-2400) / 80;
			int col = (addr-2400) % 80;
			memMdl.setValueAt(content, row, col);
		}
	}


}
