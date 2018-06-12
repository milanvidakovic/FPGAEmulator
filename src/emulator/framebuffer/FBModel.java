package emulator.framebuffer;

import javax.swing.table.AbstractTableModel;

import emulator.engine.Context;

public class FBModel extends AbstractTableModel {
	private static final long serialVersionUID = 305334635501584898L;
	
	private Context ctx;
	
	public FBModel(Context ctx) {
		this.ctx = ctx;
		
	}

	@Override
	public int getColumnCount() {
		return 80;
	}

	@Override
	public int getRowCount() {
		return 60;
	}

	@Override
	public String getColumnName(int col) {
		return "";
	}

	@Override
	public Object getValueAt(int row, int col) {
		int addr = 2400 + row*80 + col;
		return String.format("%c", ctx.memory[addr]);
	}

	@Override
	/**
	 * Ako se ova metoda ne redefinise, koristi se default renderer/editor za
	 * celiju. To znaci da, ako je kolona tipa boolean, onda ce se u tabeli
	 * prikazati true/false, a ovako ce se za takav tip kolone pojaviti
	 * checkbox.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		fireTableDataChanged();
		fireTableCellUpdated(row, col);
	}
}
