 Panda.Bind
============================

 Panda.Bind 是一个类似[Jackson](https://github.com/FasterXML/jackson) 的模块。
 它可以把一个JSON/XML字符串转换成一个Java对象(POJO or Array/Map)。

### 基本用法
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

### 将对象转为一个Json字符串
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

### 将Json字符串转为Java对象
```Json
{
    name : "Jerry",
    age : -1
}
```

转换代码:

```Java
	Pet pet = Jsons.fromJson(str, Pet.class);
	System.out.println(pet.getName()); // output: Jerry
	System.out.println(pet.getAge());  // output： -1
```



---

 - [上一篇： BIND →](bind_zh.md)
 - [下一篇： CAST →](cast_zh.md)
 - [返回目录 ↑](index_zh.md)
