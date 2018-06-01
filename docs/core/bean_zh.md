 Panda.Bean
============================

 Panda.Bean 是一个类似[Apache Commons BeanUtils](http://commons.apache.org/proper/commons-beanutils/) 的模块。


### 我该如何使用它？
 看一下下面的Sample吧

```Java
import panda.bean.BeanHandler;
import panda.bean.Beans;

public class HelloWorld {
	private String name;

	public Integer number;

	public HelloWorld other;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		HelloWorld hello = new HelloWorld();
		
		// use Beans static method
		Beans.setProperty(hello, "name", "hello");
		Beans.setBean(hello, "other.name", "world");

		String name = (String)Beans.getProperty(hello, "name");		
		System.out.println("Beans.getProperty(hello, \"name\"): " + name);

		name = (String)Beans.getBean(hello, "other.name");
		System.out.println("Beans.getBean(hello, \"other.name\"): " + name);
		
		// use BeanHandler
		BeanHandler<HelloWorld> handler = Beans.i().getBeanHandler(HelloWorld.class);
		
		handler.setPropertyValue(hello, "name", "hello");
		handler.setBeanValue(hello, "other.name", "world");

		name = (String)handler.getPropertyValue(hello, "name");		
		System.out.println("BeanHandler.getPropertyValue(hello, \"name\"): " + name);

		name = (String)handler.getBeanValue(hello, "other.name");
		System.out.println("BeanHandler.getBeanValue(hello, \"other.name\"): " + name);
	}
}

```

### 编译程序

> javac -classpath .;panda-core.jar HelloWorld.java


### 运行程序

> java -classpath .;panda-core.jar HelloWorld

Output:

	Beans.getProperty(hello, "name"): hello
	Beans.getBean(hello, "other.name"): world
	BeanHandler.getPropertyValue(hello, "name"): hello
	BeanHandler.getBeanValue(hello, "other.name"): world



---

 - [上一篇： ARGS →](args_zh.md)
 - [下一篇： BIND →](bind_zh.md)
 - [返回目录 ↑](index_zh.md)
