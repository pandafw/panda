Panda.Ioc - Annotation Loader
=============================

## Why Do I Need An Ioc Annotation Loader
Whether it is XML or JSON, you need to create a new configuration file that describes your object dependencies.

In general, the object dependencies of most applications are fixed, that is, they do not need to be modified after the project is released.
If these dependencies are written to the configuration file, the configuration file will be too large to maintain.

The ideal situation is to write the possibly changing dependencies into the configuration file, and write the not-so-changing dependencies into Java Annotations. 

First of all, this article will explain in detail if you configure your container object via annotations. 
And in the [Ioc combo loader](combo.md) acticle, we will show you how to combine multiple loaders so that you can write your object dependencies separately to xml, json, and Java annotations.


### How to use AnnotationIocLoader
Similar to JsonLoader, you can directly create a new AnnoationIocLoader
```Java
	Ioc ioc = new DefaultIoc(new AnnotationIocLoader("com.you.app.package0", "com.you.app.package1"));
```

In this way, all classes that declare the @IocBean annotation are considered as container objects.

 - com.you.app.package0
 - com.you.app.package1

By declaring **@IocBean** annotation, AnnotationIocLoader will be able to identify which classes in your specified package can be managed by the IOC container.

So, what information can be declared in **@IocBean**, how do I set the injected content for my container object? Please continue to see below...


### Specifies the name of the object

Any object managed by the Ioc container must have a name in order to obtain the object by name.
```Java
	MyObject obj = ioc.get(MyObject.class, "myObjectName");
```


Therefore, you can declare this for your object.
```Java
@IocBean(name="myObjectName")
public class MyObject {
	...
}
```

If your object name is your object class name, you can omit the name attribute, ie:
```Java
package com.my.test;

@IocBean
public class MyObject {
	...
}
```

Same effect as this:
```Java
package com.my.test;

@IocBean(name="com.my.test.myObject")
public class MyObject {
	...
}
```


### Not singleton?
Default, the objects managed by the Ioc container are singletons. 
If you do not want singletons, you can:
```Java
@IocBean(name="myObject", singleton=false)
public class MyObject {
	...
}
```

### Passing constructors for objects
In many cases, when inject a object, you need to declare information for the constructor. You can do this:
```Java
@IocBean(name="myObject", args={"'a string", "#anotherObject", "'true", "'234"})
public class MyObject {
	...
}
```

Does it seem simple? 
No matter how many parameters are there in your constructor, you can just declare them in the "**args**" field. 
So what can you inject?
The value it injects is the same as the value of the field injection. Please continue to the next section, which we explain in more detail.
If **args** is not specified, then no-argument constructor will be used.


### Inject fields for objects
Such as:

```Java
@IocBean
public class MyObject {
	
	@IocInject("myobj") // same as #myobj
	private MyObject obj;
	
	@IocInject("#another")
	private AnotherObject obj;
	
	@IocInject // first find by name (class name), then find by type
	private UserService users;
	
	...
}
```

So what can you inject? Interested people can read this: [what you can inject] (inject.md).
Of course, the way is different from Json. Please refer to the following list.

 | Annotation                                              | Description |
 |---------------------------------------------------------|-------------|
 | @IocInject("'This is apple.")                           | inject the string "This is apple." |
 | @IocInject("objName")                                   | inject the reference of container object which name is "objName" |
 | @IocInject("#objName")                                  | inject the reference of container object which name is "objName" |
 | @IocInject("#$ioc")                                     | the IOC container self |
 | @IocInject("#$Name")                                    | the name of container object which declared by @IocBean |
 | @IocInject("#$Ctx")                                     | the IOC container context |
 | @IocInject("!{'a': 'aaa', 'b': 'bbb'}")                 | JSON object |
 | @IocInject("!['1', '2', 3, 4, true ]")                  | JSON array |
 | @IocInject("${'com.my.SomeClass'@staticPropertyName}")  | inject a static java class property |
 | @IocInject("${'com.my.SomeClass'@someFunc()}")          | inject the return value of a static java class method |
 | @IocInject("${'com.my.SomeClass'@someFunc('p1',true)}") | inject the return value of a static java class method with parameters |
 | @IocInject("${xh}")                                     | inject the "xh" container object, same as "#xh" |
 | @IocInject("${xh.name}")                                | inject the property of "xh" container object |
 | @IocInject("${xh.getXXX()}")                            | inject the return value of a "xh" container object's "getXXX" method |
 | @IocInject("${xh.getXXX('some string', true, 34)}")     | inject the return value of a "xh" container object's "getXXX" method with parameters |


### How to inject private fields of parent class

```Java
	@IocBean(fields={"obj:#com.my.MyObject"})
	public AbcService extends Service {
		...
	}
```
For example, **AbcService** inherits from **Service**, but **Serivce** has a private field "private MyObject obj". How do you describe its injection?

You can use the **@IocBean** annotation and declare the fields property, describing the field to inject into the parent class.

In the above example, we will inject a "com.my.MyObject" container object into the "obj" field of the parent class.


---

 - [Previous: How to define the object →](define_en.md)
 - [Next: Event Listening →](events_en.md)
 - [Index ↑](ioc_en.md#Index)
