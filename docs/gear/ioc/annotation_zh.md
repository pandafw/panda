Panda.Ioc - Annotation 加载器
=============================

## 为什么需要 Ioc 注解加载器
无论是 XML 还是 JSON，都需要你创建一个新的配置文件，在里面描述你的对象依赖关系。

一般的来说，一个应用大多数的对象依赖关系是固定的，即在项目发布以后是不必调整的。
如果将这些依赖关系通通写到配置文件中，则会造成配置文件的庞大臃肿不易维护。

最理想的情况是，将可能变动的依赖关系写到配置文件里，而将不怎么会变动的依赖关系写成 Java 的注解 (Annotation)，如果能这样的话，一切就圆满了。

首先这篇文章，会详细讲解一下如果通过注解来配置你的容器对象，而 [Ioc复合加载器一篇](combo_zh.md)， 将会告诉你如何组合多个加载器，这样你就可以把你的对象依赖关系分别写到 xml,json,以及 Java 注解里，组合使用了。

### 如何使用 AnnotationIocLoader
同 JsonLoader 一样，你可以直接 new 一个 AnnoationIocLoader
```Java
	Ioc ioc = new DefaultIoc(new AnnotationIocLoader("com.you.app.package0", "com.you.app.package1"));
```

这样，你在

 - com.you.app.package0
 - com.you.app.package1

这两包下，所有声明了 @IocBean 这个注解的对象，都会被认为是容器对象。
通过注解 **@IocBean**， AnnotationIocLoader 就能辨别你指定的包中，哪些类是可以交由容器管理的。

那么，**@IocBean**里面还能声明什么信息，我怎么为我的容器对象设置注入内容呢？ 请继续看下面内容...


### 指定对象的名称
任何一个 Ioc 容器管理的对象，都必须有一个名字，以便通过:
```Java
	MyObject obj = ioc.get(MyObject.class, "myObjectName");
```
来获取对象。

因此，你可以为你的对象这么声明:
```Java
@IocBean(name="myObjectName")
public class MyObject {
	...
}
```

如果你的对象名字为你对象类名，你可以省略名字这个属性, 即
```Java
package com.my.test;

@IocBean
public class MyObject {
	...
}
```

同
```Java
package com.my.test;

@IocBean(name="com.my.test.myObject")
public class MyObject {
...
}
```
效果是一样的。


### 不要单例?
默认的，Ioc 容器管理的对象都是单例的，你如果不想单例，你可以:

```Java
@IocBean(name="myObject", singleton=false)
public class MyObject {
	...
}
```

### 为对象传递构造函数
很多对象注入的时候，需要为构造函数声明信息，你可以这样：

```Java
@IocBean(name="myObject", args={"'a string", "#anotherObject", "'true", "'234"})
public class MyObject {
	...
}
```

看，简单不？ 你的构造函数有多少个参数，你就一并在 "**args**" 属性里声明就好了， 那么你都能注入什么呢？
它注入的值同字段注入的值描述方式相同，请继续看下面一节，我们有更详细的解释。
若没有声明 **args**,那么就会找无参构造方法。

### 为对象的字段注入
比如:

```Java
@IocBean
public class MyObject {

	@IocInject("myobj") // 等价于 #myobj
	private MyObject obj;

	@IocInject("#another")
	private AnotherObject obj;

	@IocInject // 先按名字(类名)找,然后按类型找
	private UserService users;

	...
}
```

那么你到底能注入什么呢？ 感兴趣的同学可以看这里：[你都可以注入什么](inject_zh.md)。
当然，同Json的方式有点不同，请参照以下列表。

 | 注释                                                    | 说明         |
 |---------------------------------------------------------|-------------|
 | @IocInject("'This is apple.")                           | 注入字符串"This is apple." |
 | @IocInject("objName")                                   | 注入容器其他对象的引用 |
 | @IocInject("#objName")                                  | 注入容器其他对象的引用 |
 | @IocInject("#$ioc")                                     | 容器自身 |
 | @IocInject("#$Name")                                    | 对象的名称，即你在 @IocBean 里声明的 name |
 | @IocInject("#$Ctx")                                     | 容器上下文 |
 | @IocInject("!{'a': 'aaa', 'b': 'bbb'}")                 | JSON对象 |
 | @IocInject("!['1', '2', 3, 4, true ]")                  | JSON数组 |
 | @IocInject("${'com.my.SomeClass'@staticPropertyName}")  | 调用某 JAVA 类的静态属性 |
 | @IocInject("${'com.my.SomeClass'@someFunc()}")          | 调用某 JAVA 类的静态函数 |
 | @IocInject("${'com.my.SomeClass'@someFunc('p1',true)}") | 调用某 JAVA 类的带参数的静态函数 |
 | @IocInject("${xh}")                                     | 获得容器对象 xh ，相当于 "#xh" |
 | @IocInject("${xh.name}")                                | 容器对象某个属性 |
 | @IocInject("${xh.getXXX()}")                            | 容器对象某个方法的返回值 |
 | @IocInject("${xh.getXXX('some string', true, 34)}")     | 容器对象某个方法的返回值 |


### 如何注入父类的私有字段
答
```Java
@IocBean(fields={"obj:#com.my.MyObject"})
public AbcService extends Service {
	...
}
```

比如上例，AbcService 从 Service 继承，但是 Serivce 有一个私有字段为 "private MyObject obj"，怎么描述它的注入呢？ 

可以通过 @IocBean 注解提供 fields 属性，描述要注入父类那个字段。

比如上面的例子，我们会为父类的 "obj" 字段注入一个名为 "com.my.MyObject" 容器对象。


---

 - [上一篇：如何定义对象 →](define_zh.md)
 - [下一篇：事件监听 →](events_zh.md)
 - [返回目录 ↑](ioc_zh.md#文档目录)
