package panda.args;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import panda.io.Streams;

public class CmdLineParserTest {

	@Argument(name="A0", index=0, required=true)
	private String a0;

	@Argument(name="A1", index=1, required=true)
	private String a1;
	
	@Argument(name="AS")
	private String[] as;

	@Option(opt='i', option="ii", arg="I", usage="UI")
	private int i;

	@Option(opt='f', option="ff", arg="F", usage="UF")
	private File f;

	@Option(opt='s', option="ss", required=true, arg="S", usage="US")
	private String s;

	@Option(opt='1', option="11", hidden=true)
	private boolean b1;
	
	@Option(opt='2', option="22", hidden=true)
	private boolean b2;
	
	@Test
	public void testUsage() {
		String usage = "Usage: java panda.args.CmdLineParserTest [OPTIONS] <A0> <A1> [AS]" + Streams.EOL
			+ Streams.EOL
			+ "Arguments: " + Streams.EOL
			+ "  <A0>    " + Streams.EOL
			+ "  <A1>    " + Streams.EOL
			+ "  [AS]    " + Streams.EOL
			+ Streams.EOL
			+ "Options: " + Streams.EOL
			+ "  -f, --ff=F    UF" + Streams.EOL
			+ "  -i, --ii=I    UI" + Streams.EOL
			+ "  -s, --ss=S    US" + Streams.EOL;

		CmdLineParser clp = new CmdLineParser(this);
		Assert.assertEquals(usage, clp.usage());
	}
	
	@Test
	public void testOpt() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
	}
	
	@Test
	public void testOpt2() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-ssa", "aa0", "aa1" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
	}
	
	@Test
	public void testOption() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "--ss", "sa", "aa0", "aa1" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
	}
	
	@Test
	public void testOption2() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "--ss=sa", "aa0", "aa1" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
	}
	
	@Test
	public void testFlag() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1", "-1" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
		Assert.assertNull(as);
		Assert.assertEquals(true, b1);
	}
	
	@Test
	public void testFlag2() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1", "--11" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
		Assert.assertNull(as);
		Assert.assertEquals(true, b1);
	}
	
	@Test
	public void testFlag3() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1", "-12" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
		Assert.assertNull(as);
		Assert.assertEquals(true, b1);
		Assert.assertEquals(true, b2);
	}
	
	@Test
	public void testArgs() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1", "aa2" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
		Assert.assertArrayEquals(new String[] { "aa2" }, as);
	}
	
	@Test
	public void testRequiredOption() {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(new String[] { "aa0", "aa1" });
			clp.validate();
			Assert.fail();
		}
		catch (CmdLineException e) {
			Assert.assertEquals("The option -s,--ss is required!", e.getMessage());
		}
	}
	
	@Test
	public void testRequiredArgumentA0() {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(new String[] { "--ss=sa" });
			clp.validate();
			Assert.fail();
		}
		catch (CmdLineException e) {
			Assert.assertEquals("The argument <A0> is required!", e.getMessage());
		}
	}
	
	@Test
	public void testRequiredArgumentA1() {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(new String[] { "--ss=sa", "aa0" });
			clp.validate();
			Assert.fail();
		}
		catch (CmdLineException e) {
			Assert.assertEquals("The argument <A1> is required!", e.getMessage());
		}
	}
	
	@Test
	public void testInvalidOpt() {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(new String[] { "-v", "sa", "aa0", "aa1" });
		}
		catch (CmdLineException e) {
			Assert.assertEquals("Invalid option -v", e.getMessage());
		}
	}
	
	@Test
	public void testInvalidOption() {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(new String[] { "--v", "sa", "aa0", "aa1" });
		}
		catch (CmdLineException e) {
			Assert.assertEquals("Invalid option --v", e.getMessage());
		}
	}
	
	@Test
	public void testOptFile() throws CmdLineException {
		CmdLineParser clp = new CmdLineParser(this);
		clp.parse(new String[] { "-s", "sa", "aa0", "aa1", "-f", "test.txt" });
		Assert.assertEquals("sa", s);
		Assert.assertEquals(new File("test.txt"), f);
		Assert.assertEquals("aa0", a0);
		Assert.assertEquals("aa1", a1);
	}
}
