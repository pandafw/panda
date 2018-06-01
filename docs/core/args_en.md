 Panda.Args
============================

 Panda.Args is a small Java class library that makes it easy to parse command line options/arguments in your CUI application.

### Why should I use it?
 - It makes command line parsing very easy by using annotations
 - Generate usage text very easily
 - Designed to parse GNU-style options (where ls -lR is considered to have two options l and R).


### Example
```Java
import java.io.File;
import java.util.Arrays;

import panda.args.Argument;
import panda.args.CmdLineException;
import panda.args.CmdLineParser;
import panda.args.Option;

public class HelloWorld {

	@Argument(name="A0", index=0, required=true, usage="the 1st argument")
	private String arg0;

	@Argument(name="A1", index=1, required=true, usage="the 2nd argument")
	private String arg1;
	
	@Argument(name="AS", usage="additional arguments")
	private String[] args;

	@Option(opt='i', option="ii", arg="I", usage="integer option")
	private int num;

	@Option(opt='f', option="ff", arg="F", usage="file option")
	private File file;

	@Option(opt='s', option="ss", required=true, arg="S", usage="string option")
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
		}
		catch (CmdLineException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
			System.out.println(clp.usage());
			return;
		}
		
		if (hello.help) {
			System.out.print(clp.usage());
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
```

### Compile the source

> javac -classpath .;panda-core.jar HelloWorld.java


### Run the sample

#### no arguments

> java -classpath .;panda-core.jar HelloWorld

Output:

	ERROR: The argument <A1> is required
	
	Usage: java HelloWorld [OPTIONS] <A0> [A1] [AS]
	
	Arguments: 
	  <A0>    the 1st argument
	  [A1]    the 2nd argument (optional)
	  [AS]    additional arguments (optional)
	
	Options: 
	  -1,           flag option
	  -f, --ff=F    file option
	  -h, --help    print usage
	  -i, --ii=I    integer option
	  -s, --ss=S    string option (required)


#### pass required arguments and options

> java -classpath .;panda-core.jar HelloWorld -s hello baby
> java -classpath .;panda-core.jar HelloWorld -shello baby

Output:

	HelloWorld [arg0=baby, arg1=null, args=null, num=0, file=null, str=hello, b1=false, b2=false, help=false]


#### pass long-form options

> java -classpath .;panda-core.jar HelloWorld --ss=hello baby

Output:

	HelloWorld [arg0=baby, arg1=null, args=null, num=0, file=null, str=hello, b1=false, b2=false, help=false]


#### pass options without parameter

> java -classpath .;panda-core.jar HelloWorld -s hello -1 my baby

Output:

	HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, b1=true, b2=false, help=false]


#### pass 2 options without parameter

> java -classpath .;panda-core.jar HelloWorld -s hello -12 my baby

Output:

	HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, b1=true, b2=true, help=false]




---

 - [Next: BEAN →](bean_en.md)
 - [Index ↑](index_en.md)
