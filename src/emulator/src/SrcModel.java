package emulator.src;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import emulator.src.addsub.ADD_REGX_MREGY;
import emulator.src.addsub.ADD_REGX_MREGY_XX;
import emulator.src.addsub.ADD_REGX_REGY;
import emulator.src.addsub.ADD_REG_MXX;
import emulator.src.addsub.ADD_REG_XX;
import emulator.src.addsub.SUB_REGX_MREGY;
import emulator.src.addsub.SUB_REGX_MREGY_XX;
import emulator.src.addsub.SUB_REGX_REGY;
import emulator.src.addsub.SUB_REG_MXX;
import emulator.src.addsub.SUB_REG_XX;
import emulator.src.andorxor.AND_REGX_MREGY;
import emulator.src.andorxor.AND_REGX_MREGY_XX;
import emulator.src.andorxor.AND_REGX_REGY;
import emulator.src.andorxor.AND_REG_MXX;
import emulator.src.andorxor.AND_REG_XX;
import emulator.src.andorxor.OR_REGX_MREGY;
import emulator.src.andorxor.OR_REGX_MREGY_XX;
import emulator.src.andorxor.OR_REGX_REGY;
import emulator.src.andorxor.OR_REG_MXX;
import emulator.src.andorxor.OR_REG_XX;
import emulator.src.andorxor.XOR_REGX_MREGY;
import emulator.src.andorxor.XOR_REGX_MREGY_XX;
import emulator.src.andorxor.XOR_REGX_REGY;
import emulator.src.andorxor.XOR_REG_MXX;
import emulator.src.andorxor.XOR_REG_XX;
import emulator.src.callret.CALLC_XX;
import emulator.src.callret.CALLNC_XX;
import emulator.src.callret.CALLNO_XX;
import emulator.src.callret.CALLNP_XX;
import emulator.src.callret.CALLNZ_XX;
import emulator.src.callret.CALLO_XX;
import emulator.src.callret.CALLP_XX;
import emulator.src.callret.CALLZ_XX;
import emulator.src.callret.CALL_XX;
import emulator.src.callret.IRET;
import emulator.src.callret.RET;
import emulator.src.cmp.CMP_REGX_MREGY;
import emulator.src.cmp.CMP_REGX_MREGY_XX;
import emulator.src.cmp.CMP_REGX_REGY;
import emulator.src.cmp.CMP_REG_MXX;
import emulator.src.cmp.CMP_REG_XX;
import emulator.src.incdecneg.DEC_MREG;
import emulator.src.incdecneg.DEC_MREG_XX;
import emulator.src.incdecneg.DEC_MXX;
import emulator.src.incdecneg.DEC_REG;
import emulator.src.incdecneg.INC_MREG;
import emulator.src.incdecneg.INC_MREG_XX;
import emulator.src.incdecneg.INC_MXX;
import emulator.src.incdecneg.INC_REG;
import emulator.src.incdecneg.NEG_MREG;
import emulator.src.incdecneg.NEG_MREG_XX;
import emulator.src.incdecneg.NEG_MXX;
import emulator.src.incdecneg.NEG_REG;
import emulator.src.jmp.JC_XX;
import emulator.src.jmp.JMP_XX;
import emulator.src.jmp.JNC_XX;
import emulator.src.jmp.JNO_XX;
import emulator.src.jmp.JNP_XX;
import emulator.src.jmp.JNZ_XX;
import emulator.src.jmp.JO_XX;
import emulator.src.jmp.JP_XX;
import emulator.src.jmp.JZ_XX;
import emulator.src.load.LD_REGX_MREGY;
import emulator.src.load.LD_REGX_MREGY_XX;
import emulator.src.load.LD_REG_MXX;
import emulator.src.mov.IN_REG_XX;
import emulator.src.mov.MOV_REGX_REGY;
import emulator.src.mov.MOV_REG_XX;
import emulator.src.mov.OUT_XX_REG;
import emulator.src.muldiv.DIV_REGX_MREGY;
import emulator.src.muldiv.DIV_REGX_MREGY_XX;
import emulator.src.muldiv.DIV_REGX_REGY;
import emulator.src.muldiv.DIV_REG_MXX;
import emulator.src.muldiv.DIV_REG_XX;
import emulator.src.muldiv.MUL_REGX_MREGY;
import emulator.src.muldiv.MUL_REGX_MREGY_XX;
import emulator.src.muldiv.MUL_REGX_REGY;
import emulator.src.muldiv.MUL_REG_MXX;
import emulator.src.muldiv.MUL_REG_XX;
import emulator.src.nophalt.HALT;
import emulator.src.nophalt.NOP;
import emulator.src.pushpop.POP_REG;
import emulator.src.pushpop.PUSH_REG;
import emulator.src.pushpop.PUSH_XX;
import emulator.src.shift.SHL_REGX_MREGY;
import emulator.src.shift.SHL_REGX_MREGY_XX;
import emulator.src.shift.SHL_REGX_REGY;
import emulator.src.shift.SHL_REG_MXX;
import emulator.src.shift.SHL_REG_XX;
import emulator.src.shift.SHR_REGX_MREGY;
import emulator.src.shift.SHR_REGX_MREGY_XX;
import emulator.src.shift.SHR_REGX_REGY;
import emulator.src.shift.SHR_REG_MXX;
import emulator.src.shift.SHR_REG_XX;
import emulator.src.store.ST_MREGX_REGY;
import emulator.src.store.ST_MREGX_XX_REGY;
import emulator.src.store.ST_MXX_REG;

public class SrcModel extends AbstractTableModel {
	private static final long serialVersionUID = 8062097745546805165L;

	public String[] columnNames = { "Breakpoint", "Addr", "Content", "Assembler" };
	public List<Instruction> lines = new ArrayList<Instruction>();
	public Instruction[] addr_instr = new Instruction[65536];
	public short[] memory;

	public SrcModel(short[] memory) {
		this.memory = memory;
	}

	public SrcModel(String fileName, short[] memory) {
		this.memory = memory;
		if (fileName.endsWith(".bin")) {
			FileInputStream in;
			try {
				in = new FileInputStream(fileName);

				byte[] buffer = new byte[65536 * 2];
				in.read(buffer);

				parse(buffer);
				disassm();

				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(fileName));
				// in.readLine(); // preskočimo prvi red
				String s = in.readLine();
				in.close();
				parse(s);
				disassm();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void parse(byte[] buffer) {
		int t, t1, t2;
		for (int i = 0; i < this.memory.length; i++) {
			t1 = buffer[i * 2];
			if (t1 < 0) {
				t1 = 256 + t1;
			}
			t = t1 << 8;
			t2 = buffer[i * 2 + 1];
			if (t2 < 0) {
				t2 = 256 + t2;
			}
			t += t2;
			this.memory[i] = (short) t;
		}

	}

	private void parse(String s) {
		String[] words = s.split(" ");
		short addr = 0;
		for (int i = 0; i < words.length; i++) {
			String w = words[i];
			memory[addr++] = (short) Integer.parseInt(w, 16);
			System.out.println((addr - 1) + ": " + w + " == " + memory[addr - 1]);
		}
	}

	public void disassm() {
		int addr = 0;
		boolean finished = false;
		while (!finished) {
			Instruction instr = getInstruction(memory, addr++);
			instr.setContent();
			lines.add(instr);
			instr.tableLine = lines.size() - 1;
			if (instr.hasArgument)
				addr++;
			if (addr == 65536)
				finished = true;
			addr_instr[instr.addr] = instr;
		}
	}

	public Instruction getInstruction(short[] memory, int addr) {
		int ir = (int) memory[addr++];
		int group = (ir >> 4) & 0xf;
		int src = (ir >> 12) & 0xf;
		int dest = (ir >> 8) & 0xf;
		switch (ir & 0xf) {
		case 0: {
			// NOP/HALT GROUP
			switch (group) {
			case 0:
				return new NOP(memory, addr);
			case 0xf:
				return new HALT(memory, addr);

			}
		}
		case 1: {
			// MOV/IN/OUT GROUP
			switch (group) {
			case 0:
				return new MOV_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new MOV_REG_XX(memory, addr, src, dest);
			case 2:
				return new IN_REG_XX(memory, addr, src, dest);
			case 3:
				return new OUT_XX_REG(memory, addr, src, dest);
			}
		}
		case 2: {
			// LOAD GROUP
			switch (group) {
			case 0:
				return new LD_REGX_MREGY(memory, addr, src, dest);
			case 1:
				return new LD_REG_MXX(memory, addr, src, dest);
			case 2:
				return new LD_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		case 3: {
			// STORE GROUP
			switch (group) {
			case 0:
				return new ST_MREGX_REGY(memory, addr, src, dest);
			case 1:
				return new ST_MXX_REG(memory, addr, src, dest);
			case 2:
				return new ST_MREGX_XX_REGY(memory, addr, src, dest);
			}
		}
		case 4: {
			// JUMP GROUP
			switch (group) {
			case 0:
				return new JMP_XX(memory, addr, src, dest);
			case 1:
				return new JZ_XX(memory, addr, src, dest);
			case 2:
				return new JNZ_XX(memory, addr, src, dest);
			case 3:
				return new JC_XX(memory, addr, src, dest);
			case 4:
				return new JNC_XX(memory, addr, src, dest);
			case 5:
				return new JO_XX(memory, addr, src, dest);
			case 6:
				return new JNO_XX(memory, addr, src, dest);
			case 7:
				return new JP_XX(memory, addr, src, dest);
			case 8:
				return new JNP_XX(memory, addr, src, dest);
			}
		}
		case 5: {
			// CALL/RET GROUP
			switch (group) {
			case 0:
				return new CALL_XX(memory, addr, src, dest);
			case 1:
				return new CALLZ_XX(memory, addr, src, dest);
			case 2:
				return new CALLNZ_XX(memory, addr, src, dest);
			case 3:
				return new CALLC_XX(memory, addr, src, dest);
			case 4:
				return new CALLNC_XX(memory, addr, src, dest);
			case 5:
				return new CALLO_XX(memory, addr, src, dest);
			case 6:
				return new CALLNO_XX(memory, addr, src, dest);
			case 7:
				return new CALLP_XX(memory, addr, src, dest);
			case 8:
				return new CALLNP_XX(memory, addr, src, dest);
			case 9:
				return new RET(memory, addr, src, dest);
			case 10:
				return new IRET(memory, addr, src, dest);
			}
		}
		case 6: {
			// ADD/SUB GROUP
			switch (group) {
			case 0:
				return new ADD_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new ADD_REG_XX(memory, addr, src, dest);
			case 2:
				return new ADD_REGX_MREGY(memory, addr, src, dest);
			case 3:
				return new ADD_REG_MXX(memory, addr, src, dest);
			case 4:
				return new ADD_REGX_MREGY_XX(memory, addr, src, dest);
			case 6:
				return new SUB_REGX_REGY(memory, addr, src, dest);
			case 7:
				return new SUB_REG_XX(memory, addr, src, dest);
			case 8:
				return new SUB_REGX_MREGY(memory, addr, src, dest);
			case 9:
				return new SUB_REG_MXX(memory, addr, src, dest);
			case 10:
				return new SUB_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		case 7: {
			// AND/OR/XOR GROUP
			switch (group) {
			case 0:
				return new AND_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new AND_REG_XX(memory, addr, src, dest);
			case 2:
				return new AND_REGX_MREGY(memory, addr, src, dest);
			case 3:
				return new AND_REG_MXX(memory, addr, src, dest);
			case 4:
				return new AND_REGX_MREGY_XX(memory, addr, src, dest);
			case 5:
				return new OR_REGX_REGY(memory, addr, src, dest);
			case 6:
				return new OR_REG_XX(memory, addr, src, dest);
			case 7:
				return new OR_REGX_MREGY(memory, addr, src, dest);
			case 8:
				return new OR_REG_MXX(memory, addr, src, dest);
			case 9:
				return new OR_REGX_MREGY_XX(memory, addr, src, dest);
			case 10:
				return new XOR_REGX_REGY(memory, addr, src, dest);
			case 11:
				return new XOR_REG_XX(memory, addr, src, dest);
			case 12:
				return new XOR_REGX_MREGY(memory, addr, src, dest);
			case 13:
				return new XOR_REG_MXX(memory, addr, src, dest);
			case 14:
				return new XOR_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		case 8: {
			// CMP GROUP
			switch (group) {
			case 0:
				return new CMP_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new CMP_REG_XX(memory, addr, src, dest);
			case 2:
				return new CMP_REGX_MREGY(memory, addr, src, dest);
			case 3:
				return new CMP_REG_MXX(memory, addr, src, dest);
			case 4:
				return new CMP_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		case 9: {
			// INC/DEC/NEG GROUP
			switch (group) {
			case 0:
				return new INC_REG(memory, addr, src, dest);
			case 1:
				return new INC_MREG(memory, addr, src, dest);
			case 2:
				return new INC_MXX(memory, addr, src, dest);
			case 3:
				return new INC_MREG_XX(memory, addr, src, dest);
			case 4:
				return new DEC_REG(memory, addr, src, dest);
			case 5:
				return new DEC_MREG(memory, addr, src, dest);
			case 6:
				return new DEC_MXX(memory, addr, src, dest);
			case 7:
				return new DEC_MREG_XX(memory, addr, src, dest);
			case 8:
				return new NEG_REG(memory, addr, src, dest);
			case 9:
				return new NEG_MREG(memory, addr, src, dest);
			case 10:
				return new NEG_MXX(memory, addr, src, dest);
			case 11:
				return new NEG_MREG_XX(memory, addr, src, dest);
			}
		}
		case 10: {
			// MUL/DIV GROUP
			switch (group) {
			case 0:
				return new MUL_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new MUL_REG_XX(memory, addr, src, dest);
			case 2:
				return new MUL_REGX_MREGY(memory, addr, src, dest);
			case 3:
				return new MUL_REG_MXX(memory, addr, src, dest);
			case 4:
				return new MUL_REGX_MREGY_XX(memory, addr, src, dest);
			case 6:
				return new DIV_REGX_REGY(memory, addr, src, dest);
			case 7:
				return new DIV_REG_XX(memory, addr, src, dest);
			case 8:
				return new DIV_REGX_MREGY(memory, addr, src, dest);
			case 9:
				return new DIV_REG_MXX(memory, addr, src, dest);
			case 10:
				return new DIV_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		case 11: {
			// PUSH/POP GROUP
			switch (group) {
			case 0:
				return new PUSH_REG(memory, addr, src, dest);
			case 1:
				return new PUSH_XX(memory, addr, src, dest);
			case 2:
				return new POP_REG(memory, addr, src, dest);
			}
		}
		case 12: {
			// SHIFT GROUP
			switch (group) {
			case 0:
				return new SHL_REGX_REGY(memory, addr, src, dest);
			case 1:
				return new SHL_REG_XX(memory, addr, src, dest);
			case 2:
				return new SHL_REGX_MREGY(memory, addr, src, dest);
			case 3:
				return new SHL_REG_MXX(memory, addr, src, dest);
			case 4:
				return new SHL_REGX_MREGY_XX(memory, addr, src, dest);
			case 6:
				return new SHR_REGX_REGY(memory, addr, src, dest);
			case 7:
				return new SHR_REG_XX(memory, addr, src, dest);
			case 8:
				return new SHR_REGX_MREGY(memory, addr, src, dest);
			case 9:
				return new SHR_REG_MXX(memory, addr, src, dest);
			case 10:
				return new SHR_REGX_MREGY_XX(memory, addr, src, dest);
			}
		}
		}
		return new Instruction(memory, addr, 0, 0);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return lines.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Instruction i = lines.get(row);
		return i.toCell(col);
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
		// Prva kolona moze da se menja
		if (col < 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		System.out.println("SET BREAKPOINT AT: " + row);
		lines.get(row).breakPoint = (boolean) value;
	}

	public void reset() {
		lines.clear();
		addr_instr = new Instruction[65536];
	}

}
