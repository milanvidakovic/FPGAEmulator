package emulator.registers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import emulator.engine.Context;
import emulator.src.Instruction;

public class Register extends JLabel {
	private static final long serialVersionUID = -7236250344841037158L;
	
	public String name;
	public short val;

	public Register(String name, Context ctx) {
		this.name = name;
		this.setFont(this.getFont().deriveFont(32f));
		update();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String v = JOptionPane.showInputDialog("Enter new value");
				if (v != null) {
					if (v.startsWith("0x")) {
						Register.this.val = Short.parseShort(v.substring(2), 16);
					} else {
						Register.this.val = Short.parseShort(v);
					}
					Instruction i = ctx.mdl.addr_instr[ctx.pc.val];
					ctx.engine.refreshUI(i);
				}
			}
		});
	}
	
	public void update() {
		setText(String.format("%s: %04x", this.name, this.val));
		setToolTipText(String.format("%s: %d", this.name, this.val));
	}
}
