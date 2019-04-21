 Panda.Bind
============================

 Panda.Bind is a lightweight JSON/XML <--> Java (POJO or Array/Map) Serialize/Deserailize module.
 Similar to [Jackson](https://github.com/FasterXML/jackson).

### Basic usage
```Json
{
/**
  block comment
*/
    name : "frank",  // single line comment
    email: "frank@gmail.com", // Key-Value
    tags : ["java", "python", "linux"], // List or Array
    pets : [   // Object array
        {
            name : "tom",
            type : "dog",
            age : 10
        },
        {
            name : "jerry",
            type : "unkown",
            age : -1,
            food : "dog"
        }
    ]
}```

### Convert Java Object to Json String
```Java
	Pet pet = new Pet();
	pet.setName("tom");
	pet.setAge(10);
	Json.toJson(pet);
```

Output:

```Json
{
    name : "tom",
    age : 10
}
```

### Convert Json String to Java Object
```Json
{
    name : "Jerry",
    age : -1
}
```

Source code:

```Java
	Pet pet = Jsons.fromJson(str, Pet.class);
	System.out.println(pet.getName()); // output: Jerry
	System.out.println(pet.getAge());  // output： -1
```



---

 - [Previous： BIND →](bind_en.md)
 - [Next： CAST →](cast_en.md)
 - [Index ↑](index_ja.md)
