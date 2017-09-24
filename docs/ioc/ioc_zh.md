Panda.Ioc - Hello World
========================

Ioc简单来说就是将一部分关于对象的依赖关系的配置信息单独存储在某种介质里，并且提供一个接口帮助使用者获得这些对象。

如果从来没有接触过IOC，建议先阅读一下[这篇文章](https://www.zhihu.com/question/23277575)。

Panda.Ioc 支持Json/XML/@Annotation形式的依赖关系配置。当然，你可以扩展它，提供自己的配置文件加载方式。

下面，我先以 JSON 文件为例，给大家一个 Hello World。

## 一个简单的例子
在这个例子中，你需要一个POJO，以及一个JSON配置文件。

#### POJO源代码
panda/demo/ioc/json/Pet.java

```Java
package panda.demo.ioc.json;

import java.util.Date;

public class Pet {
	private String name;

	private Date birthday;

	private Pet friend;

	public Pet() {
	}

	public Pet(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Pet getFriend() {
		return friend;
	}

	public void setFriend(Pet friend) {
		this.friend = friend;
	}

	@Override
	public String toString() {
		return "Pet [name=" + name + ", birthday=" + birthday + ", friend=" + friend + "]";
	}
}
```

#### Json 配置文件

panda/demo/ioc/json/Pets.json

```JavaScript
{
    // 默认的，你仅仅需要直接声明每个字段的值即可，Panda.Ioc 会为你转型
    tom: {
        name: 'Tom',
        birthday: '2009-10-25 15:23:40'
    },

    jerry: {
        type: 'panda.demo.ioc.json.Pet', // 类型
        singleton: false, // 是否为单件
        args: [ 'Jerry' ], // 构造函数参数
        fields: {
            birthday : '2009-11-3 08:02:14',
            friend : '#tom'    // 指向容器里另外一个对象
        }
    }
}
```

#### 调用代码

panda/demo/ioc/json/HelloWorld.java

```Java
package panda.demo.ioc.json;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.JsonIocLoader;

public class HelloWorld {
	public static void main(String[] args) {
		Ioc ioc = new DefaultIoc(new JsonIocLoader("panda/demo/ioc/json/Pets.json"));
		Pet tom = ioc.get(Pet.class, "tom");
		System.out.println(tom);
		
		//由于配置文件中声明了类型，则可不传入类型
		Pet jerry = ioc.get(null, "jerry");
		System.out.println(jerry);
		
		// 声明了 singleton: false，那么它每次获取，都会生成一个新的实例
		Pet jerry2 = ioc.get(null, "jerry");
		System.out.println("jerry == jerry2: " + (jerry == jerry2));
		
		ioc.depose();
	}
}
```

#### 控制台输出
~~~
Pet [name=Tom, birthday=Sun Oct 25 15:23:40 GMT 2009, friend=null]
Pet [name=Jerry, birthday=Tue Nov 03 08:02:14 GMT 2009, friend=Pet [name=Tom, birthday=Sun Oct 25 15:23:40 GMT 2009, friend=null]]
jerry == jerry2: false
~~~

### 文档目录

 * [你都可以注入什么 →](inject_zh.md)
 * [如何定义对象 →](define_zh.md)
 * [Annotation加载器 →](annotation_zh.md)
 * [事件监听 →](events_zh.md)
 * [XML配置文件 →](xml_zh.md)
 * [复合加载器 →](combo_zh.md)
 * [返回上一层 ↑](../index_zh.md)
