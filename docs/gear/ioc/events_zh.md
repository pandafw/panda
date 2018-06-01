Panda.Ioc - 事件监听
============================

### Ioc有哪些事件
Panda.Ioc 容器有三种事件：

 - create事件: 当对象被创建时
 - fetch事件: 当对象被从容器中取出时
 - depose事件: 对象被销毁时

在这三种时刻，你如果想做一些特殊的操作，比如当一个数据源被销毁时，你希望能够关闭所有的连接，声明一下你想监听什么事件，以及怎么监听。

**注意**: 
	如果你的对象是 "singleton: false"，那么容器创建了对象后就会立即忘记它的存在。  
	因为鬼才知道你打算创建多少份该对象的实例，要是每份实例都记录的话，内存说不定爆了。  
	所以这样的非单例对象，在 depose 的时候，容器是不会触发 depose 事件的。  
	即使你在配置文件中声明了它，因为容器根本就不会知道这样的对象曾经存在过。  
	但是 create, fetch 事件还是会被触发的。  

### 怎么监听

```Java
@IocBean(create="onCreate", fetch="onFetch", depose="onDepose")
public class MyObject {
	public void onCreate() {
		system.out.println("onCreate()");
	}
	public void onFetch() {
		system.out.println("onFetch()");
	}
	public void onDepose() {
		system.out.println("onDepose()");
	}
}
```

**注意**： 事件监听函数必须为 public，并且不能有参数。


第一次被取出时: 
```Java
	ioc.get(MyObject.class);
```

控制台输出: 

	onCreate()
	onFetch()



### JSON文件配置
```JavaScript
xyz :{
	type : "xxx.yyy.zzz.XYZ",
	events : {
		create : "onCreate", // 创建完成后,各种属性已经被注入时
		fetch  : "onFetch",  // 每次从ioc取出时
		depose : "onDepose"  // ioc容器销毁前,一般用于清理各种资源
	}
}
```


---

 * [上一篇：Annotation加载器 →](annotation_zh.md)
 * [下一篇：XML配置文件 →](xml_zh.md)
 * [返回目录 ↑](ioc_zh.md#文档目录)
