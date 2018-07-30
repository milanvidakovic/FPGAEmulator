package emulator.src.alu;

import emulator.engine.CpuContext;
import emulator.src.Instruction;

public class ALU_B_REGX_MREGY extends Instruction {
	int type;
	
	public ALU_B_REGX_MREGY(short[] memory, int addr, int src, int dest, int type) {
		super(memory, addr, src, dest);
		super.setAssembler(Instruction.getTypeStr(type) + this.sdest + ", [" + this.ssrc + "]");
		this.type = type;
	}

	@Override
	public void exec(CpuContext ctx) {
		short old_a = ctx.getReg(this.dest).val;
		int res = 0;
		
		int fixedAddr = fix(ctx.getReg(this.src).val);
		short operand;
		if ((fixedAddr & 1) == 0)
			operand = (short)(ctx.memory[fixedAddr / 2] >> 8);
		else
			operand = (short)(ctx.memory[fixedAddr / 2] & 255);
		
		switch (type) {
		case ADD_B: res = ctx.getReg(this.dest).val + operand; break;
		case SUB_B: res = ctx.getReg(this.dest).val - operand; break;
		case AND_B: res = ctx.getReg(this.dest).val & operand; break;
		case OR_B : res = ctx.getReg(this.dest).val | operand; break;
		case XOR_B: res = ctx.getReg(this.dest).val ^ operand; break;
		case SHL_B: res = ctx.getReg(this.dest).val << operand; break;
		case SHR_B: res = ctx.getReg(this.dest).val >> operand; break;
		case MUL_B:	res = ctx.getReg(this.dest).val * operand; 
					ctx.h.val = (short)((res & 0xffff0000) >> 16);
					break;
		case DIV_B: res = ctx.getReg(this.dest).val / operand; 
					ctx.h.val = (short)(ctx.getReg(this.dest).val % operand);
					break;
		default: throw new RuntimeException("Unsupported operation type: " + type);
		}
		
		ctx.getReg(this.dest).val = (short)res;
		markFlags(res, ctx.getReg(this.dest).val, ctx);
		markOverflow(old_a, operand, ctx.getReg(this.dest).val, ctx);
		ctx.pc.val += 2;
	}
}
