 Panda.Args
============================

 Panda.Args は小さいコマンドライン引数のパーサーです。それを利用して、コマンドの引数を簡単に解析できます。
 
### 機能
 - annotationsでパラメーターを定義する
 - ヘルプの自動生成
 - GNUスタイルのオプションを解析するように設計されています（例：　ls -lRにはlとRの2つのオプションがある）

### 使い方

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
```

### ソースをコンパイル

> javac -classpath .;panda-core.jar HelloWorld.java


### プログラムを実行

#### 引数がない場合

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


#### 必須パラメーターとオプションを渡す

> java -classpath .;panda-core.jar HelloWorld **-s hello baby**  
> java -classpath .;panda-core.jar HelloWorld **-shello baby**  

Output:

> HelloWorld [**arg0=baby**, arg1=null, args=null, num=0, file=null, **str=hello**, b1=false, b2=false, help=false]


#### long-formのオプションを渡す

> java -classpath .;panda-core.jar HelloWorld **--ss=hello** baby  

Output:

> HelloWorld [arg0=baby, arg1=null, args=null, num=0, file=null, **str=hello**, b1=false, b2=false, help=false]


#### パラメータなしのオプションを渡す

> java -classpath .;panda-core.jar HelloWorld -s hello **-1** my baby  

Output:

> HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, **b1=true**, b2=false, help=false]


#### 複数のパラメータなしのオプションを渡す

> java -classpath .;panda-core.jar HelloWorld -s hello **-12** my baby  

Output:

> HelloWorld [arg0=my, arg1=baby, args=null, num=0, file=null, str=hello, **b1=true**, **b2=true**, help=false]




---

 - [次： BEAN →](bean_ja.md)
 - [インデックス ↑](index_ja.md)
