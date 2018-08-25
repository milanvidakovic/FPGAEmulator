package emulator.framebuffer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

import emulator.EmulatorMain;
import emulator.engine.CpuContext;
import emulator.engine.Engine;

public class FBViewer extends JFrame {
	private static final long serialVersionUID = -5500314457803056242L;
	public static final int TEXT_MODE = 0;
	public static final int GRAPHICS_MODE_320_240 = 1;

	public FBModel memMdl;

	BufferedImage img;
	Graphics2D gr;
	//Rectangle rect;
	Font font = new Font("Monospaced", Font.PLAIN, 15);

	public JLabel display = new JLabel();

	public Color[] backgroundColors, foregroundColors;
	/**
	 * framebuffer mode: 1 - graphics mode, 320x240 pixels; 0 - text mode, 80x60
	 * characters
	 */
	private int mode = TEXT_MODE;
	public static int titleBarHeight = 45;

	public FBViewer(CpuContext ctx, Engine eng) {
		super();
		setTitle("Frame buffer");
		memMdl = new FBModel(ctx);
		this.backgroundColors = new Color[160 * 60];
		this.foregroundColors = new Color[160 * 60];

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ctx.uart = (byte) e.getKeyChar();
				if (EmulatorMain.DEBUG)
					System.out.println("UART received: " + ctx.uart);
				Engine.irq1 = true;
			}
		});

		setSize(new Dimension(ctx.engine.main.ini.getInt("FB", "width", 400),
				ctx.engine.main.ini.getInt("FB", "height", 700)));

		img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		gr = img.createGraphics();
		gr.setFont(this.font);

		//rect = new Rectangle(0, 0, this.getWidth(), this.getHeight());
		setLocation(ctx.engine.main.ini.getInt("FB", "x", 1024), ctx.engine.main.ini.getInt("fb", "y", 0));

		setVisible(true);
	}

	public void updateCell(int addr, short content) {
		if (mode == TEXT_MODE) {
			if (addr >= Engine.VIDEO_OFFS && addr < (Engine.VIDEO_OFFS + 160 * 60)) {
				int row = (addr - Engine.VIDEO_OFFS) / 160;
				this.backgroundColors[addr - Engine.VIDEO_OFFS] = getTextColor((short) ((content >> 8) & 7));
				this.foregroundColors[addr - Engine.VIDEO_OFFS] = getTextColor((short) (~((content >> 11) & 7)));
				int fixed_addr = addr & 0xfffffffe;
				int col = (fixed_addr % 160) / 2;
				int c = (int) (content & 0xff);

				gr.setColor(this.backgroundColors[addr - Engine.VIDEO_OFFS]);
				gr.fillRect(10 + col * 10, titleBarHeight - 5 + row * 10, 10, 10);

				gr.setColor(this.foregroundColors[addr - Engine.VIDEO_OFFS]);
				gr.drawString("" + String.format("%c", c), 10 + col * 10, titleBarHeight + 5 + row * 10);

				Graphics2D g2 = (Graphics2D) getGraphics();
				g2.drawImage(img, null, 0, 0);

				if (EmulatorMain.DEBUG) {
					System.out.println("(" + row + ", " + col + "): " + String.format("%c", c));
					System.out.println("Foreground Color: " + this.foregroundColors[addr - Engine.VIDEO_OFFS]);
					System.out.println("Background Color: " + this.backgroundColors[addr - Engine.VIDEO_OFFS]);
				}
			}
		} else if (mode == GRAPHICS_MODE_320_240) {
			if (addr >= Engine.VIDEO_OFFS && addr < (Engine.VIDEO_OFFS + (320 * 240) / 2)) {
				Color p1 = getColor((short) ((content >> 4) & 7));
				Color p2 = getColor((short) ((content) & 7));
				Insets pixel = getCoordinate(addr);
				gr.setColor(p1);
				gr.fillRect(pixel.left * 2 + 8, pixel.top * 2 + titleBarHeight, 2, 2);
				gr.setColor(p2);
				gr.fillRect(pixel.left * 2 + 10, pixel.top * 2 + titleBarHeight, 2, 2);

				Graphics2D g2 = (Graphics2D) getGraphics();
				g2.drawImage(img, null, 0, 0);

				if (EmulatorMain.DEBUG) {
					System.out.println("(" + (pixel.left + 0) + ", " + (pixel.top) + "): " + p1);
					System.out.println("(" + (pixel.left + 1) + ", " + (pixel.top) + "): " + p2);
				}
			}
		}

	}

	private Insets getCoordinate(int addr) {
		if (mode == GRAPHICS_MODE_320_240) {
			int start = addr - 2400;
			int row = start / 160;
			int col = start % 160;
			return new Insets(row, col * 2, 0, 0);
		}
		return new Insets(0, 0, 0, 0);
	}
	
	private Color getTextColor(short col) {
		int c = col & 7;
		switch (c) {
		case 0:
			return Color.black;
		case 1:
			return Color.red;
		case 2:
			return Color.green;
		case 3:
			return new Color(255, 255, 0);
		case 4:
			return Color.blue;
		case 5:
			return new Color(255, 0, 255);
		case 6:
			return new Color(0, 255, 255);
		case 7:
			return Color.white;
		default:
			return Color.black;
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
		this.mode = TEXT_MODE;
	}

	public void paint(Graphics g) {
		if (EmulatorMain.DEBUG)
			System.out.println("REPAINT");
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, null, 0, 0);
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
