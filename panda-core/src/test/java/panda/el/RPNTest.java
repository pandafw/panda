package panda.el;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RPNTest {
	private ShuntingYard sy = null;

	@Before
	public void setup() {
		sy = new ShuntingYard();
	}

	private String parseRPN(String val) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : sy.parseToRPN(val)) {
			sb.append(obj);
		}
		return sb.toString();
	}

	@Test
	public void simpleRPN() throws IOException {
		Assert.assertEquals("11+1+", parseRPN("1+1+1"));
		Assert.assertEquals("11-", parseRPN("1-1"));
		Assert.assertEquals("11-1-", parseRPN("1-1-1"));
		Assert.assertEquals("52%1+", parseRPN("5%2+1"));
		Assert.assertEquals("152%+", parseRPN("1+5%2"));
	}

	private void errorRPN(String val) {
		try {
			parseRPN(val);
			Assert.fail(val);
		}
		catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void errorRPN() throws IOException {
		errorRPN("0 ? \"1\" : (\"2\"))");
		errorRPN("]");
	}

	@Test
	public void mulRPn() throws IOException {
		Assert.assertEquals("512+4*+3-", parseRPN("5+((1+2)*4)-3"));
		Assert.assertEquals("987*+65+412*-3+-*+", parseRPN("9+8*7+(6+5)*(-(4-1*2+3))"));
	}
}
