Panda.Ioc - Event Listening
============================

### What events does Ioc have?
Panda.Ioc has three events:

 - create event: When the object is created
 - fetch event: When then object is fetched from the container every time
 - depose event: When the object is to be destroyed

At these three moments, if you want to do something special, such as when a data source is about to be destroyed, 
you want to be able to close all connections, declare what events you want to listen.

**Note **: 
	If your object is "**singleton: false**" then the container will forget its existence immediately after creating the object.
	Because no one knows how many instances of the object you plan to create, if every instance is remembered, the memory may overflow.
	**So for such a non-singleton object, the container will not trigger its depose event.**
	Even if you declare it in the configuration file, because the container simply does not know that such an object ever existed.
	However, the **create and fetch** event will still be triggered.


### How to listen

```Java
@IocBean(create="onCreate", fetch="onFetch", depose="onDepose")
public class MyObject {
	public void onCreate() {
		system.out.println("onCreate()");
	}
	public void onFetch() {
		system.out.println("onFetch()");
	}
	public void onDepose() {
		system.out.println("onDepose()");
	}
}
```

**Note**: The event listener method must be public and cannot have parameters.


When the object is fetched from IOC:
```Java
	ioc.get(MyObject.class);
```

Ouput:

	onCreate()
	onFetch()



### JSON Configuration File
```JavaScript
xyz :{
	type : "xxx.yyy.zzz.XYZ",
	events : {
		create : "onCreate", // When the object is created and all properties are injected
		fetch  : "onFetch",  // When the object is fetched from IOC every time
		depose : "onDepose"  // When the object is to be destroyed
	}
}
```


---

 - [Previous: Annotation Loader →](annotation_en.md)
 - [Next: XML Configuration →](xml_en.md)
 - [Index ↑](ioc_en.md#Index)
