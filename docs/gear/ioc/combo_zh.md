Panda.Ioc - 复合加载器
============================

### 为什么需要复合加载器
Ioc 实际上一种将应用的耦合集中在一起管理的一种程序结构设计方式。耦合集中的具体形式 一般是各种格式的配置文件。比如 Spring 就有它自己的配置文件格式的规定。

在实际应用的时候，应用程序的耦合大概分做两种：

 1. 千秋万载，基本不变
 2. 部署之时，可能改变

第一种耦合关系，其实更适合写成 Annotation，这样程序发布之后就不必担心人为的错误修改了。 
而第二种则更适合写在配置文件里，你们的部署工程师可以看着你提供的小文档，根据客户现场的情况调整你的应用的各种参数。

如果将这两种耦合关系都写在配置文件里，首先部署工程师会看到一大堆你都可能忘记是什么意思的配置信息，
当这些符号映入眼帘之时，恐惧会瞬间将他吞没，嚼的骨头都不剩。 
是的，这就是面对陌生的事物，正常人类很正常的一种反应，怪不得他不是吗？

或者可怜的部署工程师手里拿的是你几个星期吐血写出来的长长的系统参数手册， 
那么写这个手册之前，感到恐怖的可能就是你了，我亲爱的同学。 
之后，他在鼓起勇气阅读的之前，通常也会倒吸一口凉气。

当然，人的智慧就是在这过程中得到了淬炼，你的意志力，你的智慧，都会得到不同程度的升华。 
但是 Panda 这个小框架开发的初衷并不是希望锻炼你，折磨你，事实上，它希望尽一切可能，让你远离这种锻炼...

...为此，我们也提供一个复合加载器，你可以将你的耦合关系写在配置文件中，或者 Annotation 中，怎样分配则由亲爱的程序员同学你来亲自决定。


### 复合加载器的使用方法
复合加载器非常简单，因为它本身并不做任何事情，它只是调用其他的加载器：

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

如上面的例子，组合加载器，组合了2个 Ioc 加载器，一个是 JsonIocLoader，一个是 AnnotationIocLoader。

ComboIocLoader 的构造函数是个字符串形变参数组，所有的参数，如果以星号 "\*" 开头，则被认为是加载器的类型，后面的参数都作为这个加载器构造函数的参数，直到遇到下一个星号 "\*" 开头的参数。

上面的例子，实际上为 Ioc 容器准备了这两个加载器，第一个是 JSON 加载器，它从 dao.js 和 service.js 这两个配置文件中读取容器对象的配置信息。  
而另外一个 Annotation 加载器，从会扫描包 com.myapp.module 以及 com.myapp.service 已经其下的子包，如果发现有容器对象（声明了 @IocBean 注解，详情请看 [Annotation 加载器](annotation_zh.md)） 就会加载。


并且这两个加载器的优先级为 **排在前面的加载器更优先**

在本节的例子中， JsonIocLoader 加载器比 AnnotationIocLoader 更加优先. 就是说，如果两个加载器都加载了同名对象，则以 JsonIocLoader 的为准。


### 你都可以复合什么?

 - XmlIocLoader
 - JsonLoader
 - AnnotationIocLoader
 - MapLoader
 - 所有实现的 IocLoader接口的类


### 类名的简写

	js         --> panda.ioc.loader.JsonIocLoader
	json       --> panda.ioc.loader.JsonIocLoader
	xml        --> panda.ioc.loader.XmlIocLoader
	anno       --> panda.ioc.loader.AnnotationIocLoader
	annotation --> panda.ioc.loader.AnnotationIocLoader


---

 - [上一篇：XML配置文件 →](xml_zh.md)
 - [返回目录 ↑](ioc_zh.md#文档目录)
