 Panda.EL
============================

### What is EL?
EL = expression language.
Panda.EL evaluate this expression and return it's result.

### Simple usage
```Java
	System.out.println(EL.calculate("3+4*5"));  // Output: 23
```

### Variable support
```Java
	Map m = new HashMap();
	m.put("a", 10);
	System.out.println(EL.calculate("a*10", m));  // Output: 100 
```

The following java type variable are supported.

 - int or Integer
 - float or Float
 - long or Long
 - boolean or Boolean
 - String
 - T[] (Array)
 - List<T>
 - Collection<T>
 - Map<?, ?>
 - Java Object (POJO)


### Supported operator

 | Operator | Operator Number | Priority | Description |
 |----------|-----------------|----------|-------------|
 | ()       |  \*             | 100      | Parenthesis |
 | ,        |  \*             | 90       | Comma between parameter |
 | @        |  2              | 1        | Static method call |
 | .        |  2              | 1        | Property or method accessor |
 | {1,2}    |  \*             | 1        | Java Array |
 | ['abc']  |  2              | 1        | Object Property or Map Element |
 | [3]      |  2              | 1        | Number indexed array/collection |
 | \*       |  2              | 3        | Multiply |
 | /        |  2              | 3        | Divide   |
 | %        |  2              | 3        | Mod      |
 | +        |  2              | 4        | Plus      |
 | -        |  2              | 4        | Minus     |
 | -        |  2              | 2        | Negative  |
 | >=       |  2              | 6        | Great Equal   |
 | <=       |  2              | 5        | Less Equal   |
 | ==       |  2              | 7        | Equal      |
 | !=       |  2              | 6        | Not Equal    |
 | !        |  2              | 7        | Not        |
 | !!       |  1              | 7        | Ignore exception and return null |
 | >        |  2              | 6        | Greater   |
 | <        |  2              | 6        | Less      |
 | &&       |  2              | 11       | Logical And |
 | \|\|     |  2              | 12       | Logical Or    |
 | A\|\|\|B |  2              | 12       | Return B if A is empty or false, else return A |
 | ?:       |  2              | 13       | Ternary   |
 | &        |  2              | 8        | Bit AND  |
 | ~        |  2              | 2        | Bit NOT  |
 | \|       |  2              | 10       | Bit OR  |
 | ^        |  2              | 9        | Bit XOR |
 | <<       |  2              | 5        | Bit Left Shift |
 | >>       |  2              | 5        | Bit Right Shift |
 | >>>      |  2              | 5        | Bit Right Shift (unsigned) |


### Like Java

Panda.EL is completely faithful to Java basic arithmetic rules and does not do some extensions, such as the most common data type conversions.  
In the process of numerical computation in Java, the type of the operation result is finally determined according to the type of both sides of the operator.

Example:  

```Java
	7/3  // return int
	(1.0 * 7)/3    // return double
	(1.0f * 7)/3   // return float
```

### Method call
Example:  

```Java
	Map map = new HashMap();
	map.put("a", new BigDecimal("7"));
	map.put("b", new BigDecimal("3"));
	assertEquals(10, EL.calculate("a.add(b).intValue()", map));
```

### Static method call
Example:  

```Java
	assertFalse((Boolean)EL.calculate("'java.lang.Boolean'@FALSE"));
	assertEquals(Boolean.TRUE, EL.calculate("'java.lang.Boolean'@parseBoolean('true')"));
```

### Some simple examples
#### General operation

```Java
	System.out.println(EL.calculate("3+2*5"));
	// Output:  13
```

#### String manipulation

```Java
	System.out.println(EL.calculate("'  abc  '.trim()"));
	// Output:  abc
```

#### Java property

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	pet.setName("GFW");
	map.put("pet", pet);
	System.out.println(EL.calculate("pet.name", map));
	// Output:  GFW
```

#### Method call

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	map.put("pet", pet);
	EL.calculate("pet.setName('XiaoBai')", map);

	System.out.println(EL.calculate("pet.getName()", map));
	// Output:  XiaoBai
```

#### Array element

```Java
	Map map = new HashMap();
	map.put("x", new String[] { "A", "B", "C" });

	System.out.println(EL.calculate("x[0].toLowerCase()"), map);
	// Output:  a
```

#### List

```Java
	Map map = new HashMap();
	map.put("x", Arrays.asList("A", "B", "C"));

	System.out.println(EL.calculate("x.get(0).toLowerCase()", map));
	// Output:  a
```

#### Map

```Java
	Map map = new HashMap();
	map.put("map", Jsons.toJson("{x:10, y:5}"));

	System.out.println(EL.calculate("map['x'] * map['y']", map));
	// Output:  50
```

#### Logical

```Java
	Map map = new HashMap();
	map.put("a",5);

	System.out.println(EL.calculate("a>10", map));
	// Output:  false

	map.put("a",20);
	System.out.println(EL.calculate("a>10", map));
	// Output:  true
```

#### Empty or exception handle

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertTrue((Boolean)EL.calculate("!!(obj.pet.name) == null", ctx));
```

#### A or B

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.calculate("!!(obj.pet.name) ||| 'cat'", map));
```

### strict mode
Defautly, EL use none strict mode (call method of null object will not raise exception)  
Example：  
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.calculate("obj.pet.name ||| 'cat'", map));
```

Run in strict mode will raise exception.  
Example:  
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertEquals("cat", EL.calculate("!!(obj.pet.name) ||| 'cat'", map));
```


### How about EL's speed?

I think it's not very fast. The principle of its work is such that each parse passes through 3 steps as below:

  - Parse the expression to a suffix expression array
  - Parse the suffix expression array into a operation tree
  - Evaluate the root node of the operation tree

Of course, I also provide a method to improve efficiency, because if each evaluation passes through these 3 steps is certainly slow, we can precompile it first:

```Java
	EL exp = new EL("a*10");  // Compile a expression and got a EL instance

	Map m = new HashMap();
	m.put("a", 10);

	System.out.println(exp.calculate(m));  // Output: 100 
```

EL.calculate() method is thread-safe.

Static call EL.calculate("xxx") will lookup EL("xxx") from a internal WeakHashMap cache. 
if not found, will generate a EL("xxx") instance and save it to cache.


---

 - [Previous: DAO →](dao_en.md)
 - [Next: INDEX →](idx_en.md)
 - [Index ↑](index_en.md)
