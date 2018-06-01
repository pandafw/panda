Panda.Ioc - Hello World
========================

Ioc simply means that some configuration information about object dependencies is stored separately in some media, and an interface is provided to help users obtain these objects.

If you have never been in contact with the IOC, it is recommended that you read [this article](http://www.baeldung.com/inversion-control-and-dependency-injection-in-spring) first. 

Panda.Ioc supports dependency configuration in the form of Json/XML/@Annotation. Of course, you can extend it and provide your own configuration file loading method.

I will use a JSON configuration file as a Hello World example as below.

## A simple example
In this example, you need a POJO java class, and a JSON configuration file.

#### POJO source code
panda/demo/ioc/json/Pet.java

```Java
package panda.demo.ioc.json;

import java.util.Date;

public class Pet {
	private String name;

	private Date birthday;

	private Pet friend;

	public Pet() {
	}

	public Pet(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Pet getFriend() {
		return friend;
	}

	public void setFriend(Pet friend) {
		this.friend = friend;
	}

	@Override
	public String toString() {
		return "Pet [name=" + name + ", birthday=" + birthday + ", friend=" + friend + "]";
	}
}
```

#### Json configuration

panda/demo/ioc/json/Pets.json

```JavaScript
{
	// By default, you only need is declaring the value of each field, Panda.Ioc will convert it's type for you
	tom: {
		name: 'Tom',
		birthday: '2009-10-25 15:23:40'
	},

	jerry: {
		type: 'panda.demo.ioc.json.Pet', // type
		singleton: false, // singleton or not
		args: [ 'Jerry' ], // constructor parameters
		fields: {
			birthday : '2009-11-3 08:02:14',
			friend : '#tom'	   // refer to another object in the container
		}
	}
}
```

#### Calling source code

panda/demo/ioc/json/HelloWorld.java

```Java
package panda.demo.ioc.json;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.JsonIocLoader;

public class HelloWorld {
	public static void main(String[] args) {
		Ioc ioc = new DefaultIoc(new JsonIocLoader("panda/demo/ioc/json/Pets.json"));
		Pet tom = ioc.get(Pet.class, "tom");
		System.out.println(tom);
		
		// because the type is declared in the configuration file, it's not need to pass the type parameter
		Pet jerry = (Pet)ioc.get(null, "jerry");
		System.out.println(jerry);
		
		// by declaring singleton: false, every time you fetch it, a new instance is generated
		Pet jerry2 = (Pet)ioc.get(null, "jerry");
		System.out.println("jerry == jerry2: " + (jerry == jerry2));
		
		ioc.depose();
	}
}
```

#### Output

	Pet [name=Tom, birthday=Sun Oct 25 15:23:40 GMT 2009, friend=null]
	Pet [name=Jerry, birthday=Tue Nov 03 08:02:14 GMT 2009, friend=Pet [name=Tom, birthday=Sun Oct 25 15:23:40 GMT 2009, friend=null]]
	jerry == jerry2: false


### Index

 - [What you can inject →](inject_en.md)
 - [How to define the object →] (define_en.md)
 - [Annotation Loader →](annotation_en.md)
 - [Event Listening →] (events_en.md)
 - [XML Configuration →] (xml_en.md)
 - [Composite Loader →] (combo_en.md)
 - [TOP ↑](../index_en.md)
