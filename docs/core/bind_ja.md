 Panda.Bind
============================

 Panda.Bind は[Jackson](https://github.com/FasterXML/jackson) のような軽量型モジュール。
 JSON/XMLの文字列をJavaオブジェクト(POJO or Array/Map)に変換できます。

### 基本的な使い方
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

### JavaオブジェクトをJson文字列に変換する
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

### Json文字列をJavaオブジェクトに変換する
```Json
{
    name : "Jerry",
    age : -1
}
```

ソースコード:

```Java
	Pet pet = Jsons.fromJson(str, Pet.class);
	System.out.println(pet.getName()); // output: Jerry
	System.out.println(pet.getAge());  // output： -1
```



---

 - [前： BIND →](bind_ja.md)
 - [次： CAST →](cast_ja.md)
 - [インデックス ↑](index_ja.md)
