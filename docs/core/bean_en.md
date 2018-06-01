 Panda.Bean
============================

 A java bean utility library like [Apache Commons BeanUtils](http://commons.apache.org/proper/commons-beanutils/) .


### Example

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

### Compile the source

> javac -classpath .;panda-core.jar HelloWorld.java


### Run the sample

> java -classpath .;panda-core.jar HelloWorld

Output:

	Beans.getProperty(hello, "name"): hello
	Beans.getBean(hello, "other.name"): world
	BeanHandler.getPropertyValue(hello, "name"): hello
	BeanHandler.getBeanValue(hello, "other.name"): world



---

 - [Previous: ARGS →](args_en.md)
 - [Next: BIND →](bind_en.md)
 - [Index ↑](index_en.md)
