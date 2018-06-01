 Panda.Args
============================

 Panda.Args 是一个小巧的命令行参数解释器，使用它可以很方便的在CUI应用程序中解析命令行参数。

### 它有哪些功能？
 - 利用注释(annotations)来定义参数
 - 自动生成HELP
 - 可以解析GNU风格的选项（其中ls -lR被认为有两个选项l和R）。


### 我该如何使用它？
 看一下下面的Sample吧

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

### 编译程序

> javac -classpath .;panda-core.jar HelloWorld.java


### 运行程序

#### 无参数

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


#### 传递参数和选项

> java -classpath .;panda-core.jar HelloWorld -s hello baby
> java -classpath .;panda-core.jar HelloWorld -shello baby

Output:

	HelloWorld [arg0=baby, arg1=null, args=null, num=0, file=null, str=hello, b1=false, b2=false, help=false]


#### 传递long-form选项

> java -classpath .;panda-core.jar HelloWorld --ss=hello baby

Output:

	HelloWorld [arg0=baby, arg1=null, args=null, num=0, file=null, str=hello, b1=false, b2=false, help=false]


#### 传递无参数选项

> java -classpath .;panda-core.jar HelloWorld -s hello -1 my baby

Output:

	HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, b1=true, b2=false, help=false]


#### 传递多个无参数选项

> java -classpath .;panda-core.jar HelloWorld -s hello -12 my baby

Output:

	HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, b1=true, b2=true, help=false]




---

 - [下一篇： BEAN →](bean_zh.md)
 - [返回目录 ↑](index_zh.md)
