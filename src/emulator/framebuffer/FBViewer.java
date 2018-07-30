package emulator.framebuffer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

import emulator.engine.CpuContext;
import emulator.engine.Engine;

public class FBViewer extends JFrame {
	private static final long serialVersionUID = -5500314457803056242L;
	// public JTable tblMem;
	public FBModel memMdl;
	// public JScrollPane src;

	BufferedImage img;
	Graphics2D gr;
	Rectangle rect;

	public JLabel display = new JLabel();

	public Color[] backgroundColors, foregroundColors;

	public FBViewer(CpuContext ctx, Engine eng) {
		super();
		setTitle("Frame buffer");
		memMdl = new FBModel(ctx);
		this.backgroundColors = new Color[160 * 60];
		this.foregroundColors = new Color[160 * 60];
		// tblMem = new JTable(memMdl);
		// src = new JScrollPane(tblMem);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ctx.uart = (byte) e.getKeyChar();
				System.out.println("UART received: " + ctx.uart);
				Engine.irq1 = true;
			}
		});

		// getContentPane().add(src, BorderLayout.CENTER);

		setSize(new Dimension(ctx.engine.main.ini.getInt("FB", "width", 400),
				ctx.engine.main.ini.getInt("FB", "height", 700)));

		img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		gr = img.createGraphics();
		rect = new Rectangle(0, 0, this.getWidth(), this.getHeight());
		setLocation(ctx.engine.main.ini.getInt("FB", "x", 1024), ctx.engine.main.ini.getInt("fb", "y", 0));

		setVisible(true);
	}

	public void updateCell(int addr, short content) {
		if (addr >= Engine.VIDEO_OFFS && addr <= (Engine.VIDEO_OFFS + 160 * 60)) {
			int row = (addr - Engine.VIDEO_OFFS) / 160;
			this.backgroundColors[addr- Engine.VIDEO_OFFS] = getColor((short) ((content >> 8) & 7));
			this.foregroundColors[addr- Engine.VIDEO_OFFS] = getColor((short) (~((content >> 11) & 7)));
			int fixed_addr = addr & 0xfffffffe;
			int col = (fixed_addr % 160) / 2;
			int c = (int) (content & 0xff);
			
			gr.setColor(this.backgroundColors[addr- Engine.VIDEO_OFFS]);
			gr.fillRect(10 + col * 10, 40 + row * 10, 10, 10);
			
			gr.setColor(this.foregroundColors[addr- Engine.VIDEO_OFFS]);
			gr.drawString("" + String.format("%c", c), 10 + col * 10, 50 + row * 10);

			Graphics2D g2 = (Graphics2D) getGraphics();
			g2.drawImage(img, null, 0, 0);

			System.out.println("(" + row + ", " + col + "): " + String.format("%c", c));
			System.out.println("Foreground Color: " + this.foregroundColors[addr]);
			System.out.println("Background Color: " + this.backgroundColors[addr]);

		}
	}

	private Color getColor(short col) {
		int c = col & 7;
		switch (c) {
		case 0:
			return Color.black;
		case 1:
			return Color.blue;
		case 2:
			return Color.green;
		case 3:
			return new Color(0, 255, 255);
		case 4:
			return Color.red;
		case 5:
			return new Color(255, 0, 255);
		case 6:
			return new Color(255, 255, 0);
		case 7:
			return Color.white;
		default:
			return Color.black;
		}
	}

	public void reset() {
		memMdl.fireTableDataChanged();
		this.backgroundColors = new Color[160 * 60];
		this.foregroundColors = new Color[160 * 60];
	}

	public void paint(Graphics g) {
		System.out.println("REPAINT");
		Graphics2D g2 = (Graphics2D) g;
		int height = img.getHeight();
		int width = img.getWidth();

		gr.setColor(Color.black);
		gr.fillRect(0, 0, width, height);
		for (int i = 0; i < 80; i++) {
			for (int j = 0; j < 60; j++) {
				int addr = Engine.VIDEO_OFFS + j * 160 + i * 2;
				int c = (int) (memMdl.ctx.memory[addr / 2] & 0xff);
				
				this.backgroundColors[addr- Engine.VIDEO_OFFS] = getColor((short) ((memMdl.ctx.memory[addr / 2] >> 8) & 7));
				this.foregroundColors[addr- Engine.VIDEO_OFFS] = getColor((short) (~((memMdl.ctx.memory[addr / 2] >> 11) & 7)));
				
				gr.setColor(this.backgroundColors[addr- Engine.VIDEO_OFFS]);
				gr.fillRect(10 + i * 10, 40 + j * 10, 10, 10);
				
				gr.setColor(this.foregroundColors[addr- Engine.VIDEO_OFFS]);
				gr.drawString("" + String.format("%c", c), 10 + i * 10, 50 + j * 10);
			}
		}

		g2.drawImage(img, null, 0, 0);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	// @Override
	// public void tableChanged(TableModelEvent ev) {
	// System.out.println("Type: " + ev.getType() + ": " + ev.getFirstRow() + ",
	// " + ev.getColumn());
	// int addr = Engine.VIDEO_OFFS + ev.getColumn()*160 + ev.getFirstRow()*2;
	// char c = (char) (memMdl.ctx.memory[addr / 2] & 0xff);
	// System.out.println("CHAR: " + c);
	// byte attr = (byte)(memMdl.ctx.memory[addr / 2] >> 8);
	// gr.drawString("" + c, ev.getColumn() * 10, ev.getFirstRow() * 10);
	// Graphics2D g2 = (Graphics2D) getGraphics();
	// g2.drawImage(img, null, 0, 0);
	// }

}
