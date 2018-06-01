Panda.Ioc - Composite Loader
============================

### Why do I need a composite loader?
Ioc is actually a program structure design method that centralizes the coupling of applications together. 
The specific form of coupling is generally a configuration file in various formats. 
For example, Spring has its own configuration file format.

In actual situations, the coupling of applications is roughly divided into two types:

 1. almost unchanged
 2. maybe changed at the time of deployment

The first kind of coupling relationship is actually better suited to be written as an Annotation, 
so that you don't need to worry about human error modification after the program is released. 
The second one is more suitable to be written in the configuration file. 
Your deployment engineer can look at the small documents you provide, 
and adjust various parameters of your application according to the customer's conditions.

If these two kinds of coupling relationships are written in the configuration file, 
the deployment engineer will first see a lot of configuration information that you may forget its meanings. 
When these symbols come into view, the fear will instantly engulf him. 
No chewing bones left. Yes, this is a reaction to normal people in the face of strange things.

Or the poor deployment engineer has a long system parameter manual that you have written in the blood for several weeks. 
Before writing this manual, you may feel terrible that you, my dear friend. 
Afterwards, he usually sucks a cold air before he has the courage to read.

Of course, human wisdom is tempered in this process. Your willpower and your wisdom will all be sublimated to varying degrees. 
But the original purpose of this small framework (Panda) is not to train you and torture you. 
In fact, it wants to do everything possible to keep you away from this exercise...

For this purpose, we also provide a composite loader. 
You can write your coupling relationship in a configuration file, or Annotation. 
How to allocate it is decided by you yourself.


### How to use the composite loader
The composite loader is very simple because it doesn't do anything by itself, it just calls other loaders:

```Java
	ComboIocLoader loader = new ComboIocLoader(
		"*js",
		"ioc/dao.js",
		"ioc/service.js",
		"*anno",
		"com.myapp.module",
		"com.myapp.service"
	);
```

As in the above example, the combination loader combines two Ioc loaders, one JsonIocLoader and one AnnotationIocLoader.

The constructor of ComboIocLoader is an array of string parameters. 
All parameters, if they start with asterisk "\*", are considered to be the type of loader. 
The following parameters are used as parameters of the constructor of this loader until they encounter the next parameter that starts with the asterisk "\*".

The above example actually prepares these two loaders for the Ioc container. 
The first is the JSON loader, which reads the container object configuration information from the dao.js and service.js configuration files.
Another Annotation loader will scan packages com.myapp.module and it's sub-packages to load container objects (declared by @IocBean annotation, see [Annotation Loader](Annotation.md)).


The priority of these two loaders is the parameter order.

In the examples in this section, the JsonIocLoader loader takes precedence over the AnnotationIocLoader. 
That is, if both loaders load an object of the same name, the JsonIocLoader prevails.


### What can you compound?

 - XmlIocLoader
 - JsonLoader
 - AnnotationIocLoader
 - MapLoader
 - All classes that implement the IocLoader interface


### Shorthand for class names

	js         --> panda.ioc.loader.JsonIocLoader
	json       --> panda.ioc.loader.JsonIocLoader
	xml        --> panda.ioc.loader.XmlIocLoader
	anno       --> panda.ioc.loader.AnnotationIocLoader
	annotation --> panda.ioc.loader.AnnotationIocLoader


---

 - [Previous: XML Configuration →](xml_en.md)
 - [Index ↑](ioc_en.md#Index)
