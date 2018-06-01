Panda.Ioc - How to define an object
====================================

For Panda Ioc, its configuration file is a nested "name-value pair collection", or a collection of Maps. 
In fact, it first parses the entire configuration file into a Map.

If a Map contains only the following keys, it is considered a **strictly defined** inject object:

 | Keyword   | Description |
 |-----------|-------------|
 | type      | object type |
 | singleton | singleton or not |
 | scope     | object life cycle |
 | events    | event listeners |
 | args      | constructor parameters |
 | fields    | field settings |
 | factory   | factory method |

Otherwise, the Map is considered to be an **anonymous object** that declares each field of the object.


### Anonymous object
If a field's value is such a "**Strictly defined** Map" as above, then it is naturally considered to be an anonymous object.

Because this object has no name, you cannot obtain it directly through the Ioc interface. It is hidden in a field of an object.

The anonymous object is not singleton, even if **singleton: true** is declared. 
If its host is a singleton object, it will naturally only be created once. 
Otherwise, it will be created each time the host is created.


JSON configuration file:
```JavaScript
	tom : {
		name : 'Tom',
		// Note: here, the 'friend' field directly declares another object
		friend : {
			type : 'panda.demo.ioc.json.Pet',
			fields : {
				name : 'Jerry'
			}
		}
	}
```

Calling source code:
```Java
	Ioc ioc = new DefaultIoc(new JsonLoader("panda/demo/ioc/json/Pets.json"));
	Pet pet = ioc.get(Pet.class, "tom");
	System.out.println(pet.getFriend().getName());
	ioc.depose();
```

Output:

	Jerry


### Factory method
#### Syntax 1 (full_class_name@method_name):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "com.my.PetFactory@create"
}
```
Where **com.my.PetFactory** is the full class name of the factory class, **create** is the factory method, and its parameter is "**Tom**".


#### Syntax 2 (#ioc_object_name@method_name):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "#PetFactory.create"
}
```
Where **PetFactory** is the name of the Ioc object, **create** is the factory method and its parameter is "**Tom**".


#### Syntax 3 (${el_expression}):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "${'com.my.PetFactory'@create()}"
}
```
Where ${'com.my.PetFactory'@create()} is an EL expression. The method of writing an EL expression can refer to [here] (inject_en.md#el%20expression).

Note that the EL expression ignores the parameter setting **args : [ "Tom" ]**.

For a more detailed description, please refer to [EL Expression](../core/el_en.md).


### Life cycle
The default scope is the **app**, which coexists with the ioc container.
Additional scopes are **request** and **session** or customized scope. 
The IOC itself will almost never use a scope other than the **app**. 
The MVC module will use the **request** and **session** scope.
In general, you do not need to set the scope value.

---

 - [Previous: What you can inject →](inject_en.md)
 - [Next: Annotation Loader →](annotation_en.md)
 - [Index ↑](ioc_en.md#Index)
