Panda.Ioc - 如何定义对象
========================

对于 Panda Ioc 来说，它面对的配置文件就是层层嵌套的 "名值对集合"，或者说是 Map 集合。 
事实上，它是先把整个配置文件解析成 Map 再做判断的。

如果一个 Map 仅包括如下的键，则被认为是一个**严格定义**的注入对象：

 | 关键字 | 说明 |
 |-----------|------|
 | type      | 对象类型 |
 | singleton | 是否单例 |
 | scope     | 生命周期范围 |
 | events    | 监听事件 |
 | args      | 构造函数参数 |
 | fields    | 字段设定 |
 | factory   | 工厂方法 |

否则，这个 Map 会被认为，是在声明对象的每个字段的**匿名对象**。

### 匿名对象
如果，一个字段的值就是上述这样的一个 “**严格定义**的Map”，那么自然会被认为是一个匿名对象。

因为这个对象没有名字，你不能通过Ioc接口直接获得，它隐藏在某个对象的某个字段里面。

匿名对象，没有所谓的单例，即使声明了 **singleton: true** 也没有用。
如果它的宿主是单例，它自然只会被创建一次。否则，每次宿主被创建的时候，它都会被创建。

JSON 配置文件：
```JavaScript
	tom : {
		name : 'Tom',
		// 请注意，在这里， 'friend' 字段，直接声明了另外一个对象
		friend : {
			type : 'panda.demo.ioc.json.Pet',
			fields : {
				name : 'Jerry'
			}
		}
	}
```

调用代码:
```Java
	Ioc ioc = new DefaultIoc(new JsonLoader("panda/demo/ioc/json/Pets.json"));
	Pet pet = ioc.get(Pet.class, "tom");
	System.out.println(pet.getFriend().getName());
	ioc.depose();
```

控制台输出:

	Jerry


### 工厂方法
#### 语法1 (类全名@方法名):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "com.my.PetFactory@create"
}
```
其中**com.my.PetFactory**是工厂类的类全名, **create**是工厂方法, 其参数是"**Tom**"。


#### 语法2 (#Ioc对象名@方法名):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "#PetFactory.create"
}
```
其中**PetFactory**是Ioc对象名, **create**是工厂方法, 其参数是"**Tom**"。


#### 语法3 (${EL表达式}):
```JavaScript
{
	type : "panda.demo.ioc.Pet",
	args : [ "Tom" ],
	factory: "${'com.my.PetFactory'@create()}"
}
```
其中 ${'com.my.PetFactory'@create()} 是EL表达式, EL表达式的书写方法可参照[这里](inject_zh.md#el表达式)。

请注意，EL表达式忽略了参数设置 **args : [ "Tom" ]**。

更详细的说明，请参照 [EL表达式](../core/el_zh.md)。


### 生命周期范围
默认的scope是app, 与ioc容器共存亡。
可选的还有request和session或者自定义的scope, IOC单独几乎不会用，MVC模块会用到request,session的scope。
一般情况下,不需要也不应该设置scope的值。


---

 - [上一篇：你都可以注入什么 →](inject_zh.md)
 - [下一篇：Annotation加载器 →](annotation_zh.md)
 - [返回目录 ↑](ioc_zh.md#文档目录)
