 Panda.Core.EL
============================

### EL表达式是啥东东
EL (expression language) 简单来说就是一个表达式字符串。
Panda.EL就是执行这个EL并且返回结果的引擎。

### 最简单的用法
```Java
	System.out.println(EL.eval("3+4*5"));  // 将打印 23，够简单吧
```

### 它支持变量，比如
```Java
	Map m = new HashMap();
	m.put("a", 10);
	System.out.println(EL.eval("a*10", m));  // 将打印 100 
```

你可以为你的表达式随意设置变量的值。它支持如下类型的 Java 数据

 - 整型 - int 或 Integer
 - 浮点 - float 或 Float
 - 长整 - long 或 Long
 - 布尔 - boolean 或 Boolean
 - 字符串 - String
 - 数组 - T[]
 - 列表 - List<T>
 - 集合 - Collection<T>
 - Map - Map<?, ?>
 - 普通 Java 对象


### 支持什么样的操作符

 | 符号     | 操作数  |  权重  |  说明              |
 |----------|---------|-------|--------------------|
 | ()       |  \*     | 100   | 括号，优先计算       |
 | ,        |  \*     | 90    | 逗号操作符,将左右两边的数据组织成一个数据  |
 | @        |  2      | 1     | 静态方法的调用 |
 | .        |  2      | 1     | 访问对象的属性，或者Map的值，或者方法调用 |
 | {1,2}    |  \*     | 1     | Java数组 |
 | ['abc']  |  2      | 1     | Java 对象 Map按键值获得值 |
 | [3]      |  2      | 1     | 数字，列表，或者集合的下标访问符号 |
 | \*       |  2      | 3     | 乘       |
 | /        |  2      | 3     | 整除      |
 | %        |  2      | 3     | 取模      |
 | +        |  2      | 4     | 加        |
 | -        |  2      | 4     | 减        |
 | -        |  2      | 2     | 负        |
 | >=       |  2      | 6     | 大于等于   |
 | <=       |  2      | 5     | 小于等于   |
 | ==       |  2      | 7     | 等于      |
 | !=       |  2      | 6     | 不等于    |
 | !        |  2      | 7     | 非        |
 | !!       |  1      | 7     | 忽略異常，返回null |
 | >        |  2      | 6     | 大于      |
 | <        |  2      | 6     | 小于      |
 | &&       |  2      | 11    | 逻辑与    |
 | \|\|     |  2      | 12    | 逻辑或    |
 | A\|\|\|B |  2      | 12    | 若A为空或False返回B,返否則回A  |
 | ?:       |  2      | 13    | 三元运算   |
 | &        |  2      | 8     | 位运算，与  |
 | ~        |  2      | 2     | 位运算，非  |
 | \|       |  2      | 10    | 位运算，或  |
 | ^        |  2      | 9     | 位运算，异或 |
 | <<       |  2      | 5     | 位运算，左移 |
 | >>       |  2      | 5     | 位运算，右移 |
 | >>>      |  2      | 5     | 位运算，无符号右移 |


只要支持的操作符，它的优先级以及行为会和 Java 的表达式一致。如果你发现不一致 别犹豫，给我报 Issue 吧。 


### 忠于 Java

Panda.EL 完全忠实于 JAVA 基本运算规则, 并没有做一些扩展, 比如最常见的数据类型转换。  
在 JAVA 中进行数值运算的过程中, 是根据运算符两边的类型而最终决定运算结果的类型的。

比如:  

```Java
	7/3  // 将返回int型
	而 
	(1.0 * 7)/3    // 返回double
	(1.0f * 7)/3   // 则返回float
```

### 支持对象方法调用
比如:  

```Java
	Map map = new HashMap();
	map.put("a", new BigDecimal("7"));
	map.put("b", new BigDecimal("3"));
	assertEquals(10, EL.eval(map, "a.add(b).intValue()"));
```

### 支持静态方法调用
比如:  

```Java
	assertFalse((Boolean)EL.eval("'java.lang.Boolean'@FALSE"));
	assertEquals(Boolean.TRUE, EL.eval("'java.lang.Boolean'@parseBoolean('true')"));
```

### 一些表达式的例子
#### 普通运算

```Java
	System.out.println(EL.eval("3+2*5"));
	// 输出为  13
```

#### 字符串操作

```Java
	System.out.println(EL.eval("'  abc  '.trim()"));
	// 输出为  abc
```

#### Java 对象属性访问调用

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	pet.setName("GFW");
	map.put("pet", pet);
	System.out.println(EL.eval("pet.name", map));
	// 输出为  GFW
```

#### 函数调用

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	map.put("pet", pet);
	EL.eval("pet.setName('XiaoBai')", map);

	System.out.println(EL.eval("pet.getName()", map));
	// 输出为  XiaoBai
```

#### 数组访问

```Java
	Map map = new HashMap();
	map.put("x", new String[] { "A", "B", "C" });

	System.out.println(EL.eval("x[0].toLowerCase()"), map);
	// 输出为  a
```

#### 列表访问

```Java
	Map map = new HashMap();
	map.put("x", Arrays.asList("A", "B", "C"));

	System.out.println(EL.eval("x.get(0).toLowerCase()", map));
	// 输出为  a
```

#### Map 访问

```Java
	Map map = new HashMap();
	map.put("map", Jsons.toJson("{x:10, y:5}"));

	System.out.println(EL.eval("map['x'] * map['y']", map));
	// 输出为  50
```

#### 判断

```Java
	Map map = new HashMap();
	map.put("a",5);

	System.out.println(EL.eval("a>10", map));
	// 输出为  false

	map.put("a",20);
	System.out.println(EL.eval("a>10", map));
	// 输出为  true
```

#### 空值或异常的处理

例如obj.pet.name中的pet是null, 可以捕捉异常然后表达式的值为null

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertTrue((Boolean)EL.eval("!!(obj.pet.name) == null", ctx));
```

#### A或者B

有空值/异常处理后,有时候还需要填充一个默认值

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.eval("!!(obj.pet.name) ||| 'cat'", map));
```

### 严格模式(strict)和非严格模式
缺省情况下，EL使用非严格模式(not strict)，即空对象的函数调用不会抛出异常。
比如：
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.eval("obj.pet.name ||| 'cat'", map));
```

如果是严格模式(strict)，空对象的函数调用会抛出异常，比如：  
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertEquals("cat", EL.eval("!!(obj.pet.name) ||| 'cat'", map));
```



### 它速度怎么样？

我觉得它速度不怎么样。它的工作的原理是这样的，每次解析都经过如果下三步：

 - 解析成后缀表达式形式的一个队列
 - 将后缀表达式解析成一棵运算树
 - 对运算树的根结点进行运算

当然我也提供了一个提升效率的手段，因为如果每次计算都经过这三个步骤当然慢， 所以我们可以对它先预编译：

```Java
	EL exp = new EL("a*10");  // 预编译结果为一个 EL 对象

	Map m = new HashMap();
	m.put("a", 10);

	System.out.println(exp.eval(m));  // 将打印 100 
```

EL在实例化时就会对表达式进行预编译，会直接编译成运算树，当调用eval方法时，就不用再耗时的编译动作了。它的 eval 函数是线程安全的。

静态函数EL.eval("xxx")会通过内部WeakHashMap缓存中查找EL("xxx")的对象，如果没找到，就会生成一个，并且把它保存至缓存里。


---

 - [上一篇：DAO模块 →](dao_zh.md)
 - [下一篇：全文检索 →](idx_zh.md)
 - [返回目录 ↑](index_zh.md)
