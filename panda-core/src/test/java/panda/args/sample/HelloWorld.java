package panda.args.sample;

import java.io.File;
import java.util.Arrays;

import panda.args.Argument;
import panda.args.CmdLineException;
import panda.args.CmdLineParser;
import panda.args.Option;

public class HelloWorld {

	@Argument(name="A0", index=0, required=true, usage="the 1st argument")
	private String arg0;

	@Argument(name="A1", index=1, usage="the 2nd argument (optional)")
	private String arg1;
	
	@Argument(name="AS", usage="additional arguments (optional)")
	private String[] args;

	@Option(opt='i', option="ii", arg="I", usage="integer option")
	private int num;

	@Option(opt='f', option="ff", arg="F", usage="file option")
	private File file;

	@Option(opt='s', option="ss", required=true, arg="S", usage="string option (required)")
	private String str;

	@Option(opt='1', usage="flag option")
	private boolean b1;
	
	@Option(opt='2', option="F2", hidden=true)
	private boolean b2;

	@Option(opt='h', option="help", usage="print usage")
	private boolean help;
	
	public static void main(String[] args) {
		HelloWorld hello = new HelloWorld();
		
		CmdLineParser clp = new CmdLineParser(hello);
		try {
			clp.parse(args);

			if (hello.help) {
				System.out.print(clp.usage());
				return;
			}

			clp.validate();
		}
		catch (CmdLineException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
			System.out.println(clp.usage());
			return;
		}
		
		System.out.println(hello.toString());
	}

	@Override
	public String toString() {
		return "HelloWorld [arg0=" + arg0 + ", arg1=" + arg1 + ", args=" + Arrays.toString(args) + ", num=" + num
				+ ", file=" + file + ", str=" + str + ", b1=" + b1 + ", b2=" + b2 + ", help=" + help + "]";
	}

}
