package instructions.andorxor;

import org.junit.Test;

import emulator.engine.Context;
import emulator.src.andorxor.AND_REGX_REGY;
import emulator.src.incdecneg.NEG_REG;


public class TestAndOrXor {

	@Test
	public void testAND_A_B() {
		Context ctx = new Context();
		int src = 0, dest = 1;
		AND_REGX_REGY n = new AND_REGX_REGY(new short[2], 1, src, dest);
		ctx.getReg(src).val = 1;
		ctx.getReg(dest).val = 2;
		n.exec(ctx);
		assert (ctx.getReg(src).val == 0);
		assert ((ctx.f.val & 8) == 1);
		assert ((ctx.f.val & 1) == 1);
		ctx.getReg(src).val = 1;
		ctx.getReg(dest).val = 3;
		n.exec(ctx);
		assert (ctx.getReg(src).val == 1);
		assert ((ctx.f.val & 8) == 1);
		assert ((ctx.f.val & 1) == 0);
		ctx.getReg(src).val = -1;
		ctx.getReg(dest).val = (short) 0xffff;
		n.exec(ctx);
		assert (ctx.getReg(src).val == -1);
		assert ((ctx.f.val & 8) == 0);
		assert ((ctx.f.val & 1) == 0);

	}
	
	@Test
	public void testNEG_A() {
		Context ctx = new Context();
		int src = 0;
		NEG_REG n = new NEG_REG(new short[2], 1, src, 0);
		ctx.getReg(src).val = 1;
		n.exec(ctx);
		assert (ctx.getReg(src).val == -1);
		assert ((ctx.f.val & 8) == 0);
		n.exec(ctx);
		assert (ctx.getReg(src).val == 1);
		assert ((ctx.f.val & 8) == 1);
	}

}