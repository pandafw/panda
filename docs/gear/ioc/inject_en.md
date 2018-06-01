Panda.Ioc - What can you inject
=================================

### Where to inject?
There are two entries where you can inject values ​​into the object.

 1. Constructor parameters
 2. Property


#### Inject parameters into the constructor
Your JSON configuration file will be like this: 
```JavaScript
{
	tom : {
		type : 'panda.demo.ioc.json.Pet',
		args : ['Tom']
	}
}
```
The value of **args** is an array where each element will correspond to a parameter of the constructor.
Of course, you must ensure that you have such a constructor.


#### Injecting parameters into properties
Your JSON configuration file will be like this: 
```JavaScript
{
	tom : {
		type : 'panda.demo.ioc.json.Pet',
		fields : {
			name : 'Tom'
		}
	}
}
```

If you do not need to write type, then you can use shorthand mode:
```JavaScript
{
	tom: { name: 'Tom' }
}
```

The value can be more than just a string, it can also be of the following type.

#### Boolean
```JavaScript
{
	tom : { dead: true }
}
```

#### Number
```JavaScript
{
	tom : { age: 24 }
}
```

#### Anonymous Object
```JavaScript
{
	tom : {
		friend: {
			type : 'panda.demo.ioc.json.Pet',
			fields : { name : 'Jerry' }
		}
	}
}
```
For anonymous objects, see [here](define_en.md).

#### Reference
```JavaScript
{
	tom : { friend: '#jerry' }
}
```
*friend: '#jerry'*  will refer to another object named "**jerry**" in the container

#### IOC container ifself
```JavaScript
{
	tom : { myIoc : '#$Ioc' }
}
```
A special reference, case insensitive, the value is the Ioc container itself

#### Object Name
```JavaScript
{
	tom : { beanName : '#$Name'}
}
```
A special reference, case insensitive, the value is the name of the object, ie "tom"

#### IOC Container Context
```JavaScript
{
	tom : { myContext : '#$Ctx' }
}
```
A special reference, case insensitive, the value is the context (interface panda.ioc.IocContext) of the current IOC container

#### JSON Object/Array
If your object's field is an array, collection, or Map, it's natural to set its value using JSON, isn't it?
```JavaScript
{
	tom : { favorites: [ 'banana', 'bug' ] }
}
```

or
```JavaScript
{
	tom : { favorites: "![ 'banana', 'bug' ]" }
}
```

#### EL Expression
This is an extremely flexible injection method that allows you to do almost anything. 
Because it allows you to directly call a Java method.

For a more detailed description, see [EL Expression](../core/el_en.md).

Here are just a few of the major ways of using:

Static Property
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticPropertyName}" }
}
```

Static Method
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticFunc()}" }
}
```

Static Method with parameters (parameters can be any type)
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticFunc('param', true)}" }
}
```

Object in the IOC container
```JavaScript
{
	tom : { friend: '${jerry}' },
	jerry : { type : 'com.my.Pet' }
}
```

Property of the object in the IOC container
```JavaScript
{
	tom : { oneField : '${jerry.name}' } ,
	jerry : { type: 'com.my.Pet'}
}
```

Method's return value of the object in the IOC container
```JavaScript
{
	tom : { oneField : '${jerry.getXXX()}' } ,
	jerry : { type: 'com.my.Pet'}
}
```

Method's return value of the object in the IOC container (with parameters)
```JavaScript
{
	tom : { oneField : '${jerry.getXXX("some string", true, 34)}' } ,
	jerry : { type: 'com.my.Pet'}
}
```



---

 - [Previous: Hello World →](ioc_en.md)
 - [Next: How to define the object →](define_en.md)
 - [Index ↑](ioc_en.md#Index)
