Panda.Ioc - 你都可以注入什么
============================

### 从哪里注入？
你可以向对象注入值的位置有两个

 1. 构造函数参数
 2. 属性

#### 向构造函数里注入参数
你的 JSON 配置文件会是这样：
```JavaScript
{
	tom : {
		type : 'panda.demo.ioc.json.Pet',
		args : ['Tom']
	}
}
```
**args**的值是一个数组，里面每一个元素都将对应构造函数的一个参数。
当然，你必须确保你得有这样的构造函数。

#### 向属性注入参数
你的 JSON 配置文件会是这样：
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

如果你不需要写 type，那么你可以用简写模式:
```JavaScript
{
	tom: { name: 'Tom' }
}
```

值可以不仅仅是字符串，它还可以是以下类型。

#### 布尔
```JavaScript
{
	tom : { dead: true }
}
```

#### 数字
```JavaScript
{
	tom : { age: 24 }
}
```

#### 匿名对象
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
关于匿名对象，请看[这里](define_zh.md)。

#### 引用
```JavaScript
{
	tom : { friend: '#jerry' }
}
```
*friend: '#jerry'* 将会得到容器中另外一个名为jerry的对象

#### 容器自身
```JavaScript
{
	tom : { myIoc : '#$Ioc' }
}
```
一种特殊的引用，大小写不敏感，值就是 Ioc 容器本身

#### 对象的名称
```JavaScript
{
	tom : { beanName : '#$Name'}
}
```
一种特殊的引用，大小写不敏感，值就是对象的名称，即 "tom"

#### 容器上下文
```JavaScript
{
	tom : { myContext : '#$Ctx' }
}
```
一种特殊的引用，大小写不敏感，值就是当前容器的上下文环境接口 panda.ioc.IocContext

#### JSON 对象/数组
如果你对象某个字段是数组，集合，或者Map， 用JSON可以很自然为其设置值，不是吗？
```JavaScript
{
	tom : { favorites: [ 'banana', 'bug' ] }
}
```

或者
```JavaScript
{
	tom : { favorites: "![ 'banana', 'bug' ]" }
}
```

#### EL表达式
这是个极度灵活的注入方式，它几乎可以让你做任何事情。 因为它允许你直接调用一个 JAVA 函数。

更详细的说明，请参看 [EL表达式](../core/el_zh.md)。

下面只是列出主要的几种应用方式

静态属性
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticPropertyName}" }
}
```

静态函数
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticFunc()}" }
}
```

带参数的静态函数(参数可以是任何种类的值)
```JavaScript
{
	tom : { staticField : "${'com.my.SomeClass'@staticFunc('param', true)}" }
}
```

容器中的对象
```JavaScript
{
	tom : { friend: '${jerry}' },
	jerry : { type : 'com.my.Pet' }
}
```

容器对象某个属性
```JavaScript
{
	tom : { oneField : '${jerry.name}' } ,
	jerry : { type: 'com.my.Pet'}
}
```

容器对象某个方法的返回值
```JavaScript
{
	tom : { oneField : '${jerry.getXXX()}' } ,
	jerry : { type: 'com.my.Pet'}
}
```

容器对象某个方法的返回值，带参数(参数可以是任何种类的值)
```JavaScript
{
	tom : { oneField : '${jerry.getXXX("some string", true, 34)}' } ,
	jerry : { type: 'com.my.Pet'}
}
```



---

 - [上一篇：Hello World →](ioc_zh.md)
 - [下一篇：如何定义对象 →](define_zh.md)
 - [返回目录 ↑](ioc_zh.md#文档目录)
