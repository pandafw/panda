 Panda.EL
============================

### ELとは
EL = expression language.
Panda.ELはこのような式を評価して、その結果を返すモジュールです。


### シンプルな使い方
```Java
	System.out.println(EL.eval("3+4*5"));  // Output: 23
```

### 変数のサポート
```Java
	Map m = new HashMap();
	m.put("a", 10);
	System.out.println(EL.eval("a*10", m));  // Output: 100 
```

以下のJava型変数がサポートされています。

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


### サポートされている演算子

 | 演算子 | 引数の数 | 優先順位 | 説明 |
 |----------|-----------------|----------|-------------|
 | ()       |  \*             | 100      | 括弧 |
 | ,        |  \*             | 90       | パラメータ間のコンマ |
 | @        |  2              | 1        | 静的メソッド呼び出し |
 | .        |  2              | 1        | プロパティまたはメソッドのアクセサ |
 | {1,2}    |  \*             | 1        | Java 配列 |
 | ['abc']  |  2              | 1        | ObjectのプロパティまたはMapの要素 |
 | [3]      |  2              | 1        | 番号付きインデックスの配列またはコレクション |
 | \*       |  2              | 3        | 乗算 |
 | /        |  2              | 3        | 除算   |
 | %        |  2              | 3        | 剰余      |
 | +        |  2              | 4        | 加算      |
 | -        |  2              | 4        | 減算     |
 | -        |  2              | 2        | マイナス記号  |
 | >=       |  2              | 6        | 左辺が右辺以上であればtrue   |
 | <=       |  2              | 5        | 左辺が右辺以下であればtrue   |
 | ==       |  2              | 7        | 左辺と右辺が等しければtrue      |
 | !=       |  2              | 6        | 左辺と右辺が等しくなければtrue    |
 | !        |  2              | 7        | 式がfalseの場合はtrue   |
 | !!       |  1              | 7        | exceptionを無視してnullを返す |
 | >        |  2              | 6        | 左辺が右辺より大きければtrue   |
 | <        |  2              | 6        | 左辺が右辺より小さければtrue  |
 | &&       |  2              | 11       | 左辺右辺がともにtrueの場合はtrue |
 | \|\|     |  2              | 12       | 左辺右辺どちらかがtrueの場合はtrue    |
 | A\|\|\|B |  2              | 12       | Aが空まあはfalseの場合、Bを返す、そうでない場合Aを返す |
 | ?:       |  2              | 13       | 条件演算子（?:）   |
 | &        |  2              | 8        | 論理積。左右双方の式にセットされているビット  |
 | ~        |  2              | 2        | ビットを反転  |
 | \|       |  2              | 10       | 論理和。左右いずれかの式にセットされているビット  |
 | ^        |  2              | 9        | Bit XOR |
 | <<       |  2              | 5        | ビットを左シフト |
 | >>       |  2              | 5        | ビットを右シフト |
 | >>>      |  2              | 5        | ビットを右シフト、左端は0埋め |


### Javaの構文に似ています。

Panda.ELはJavaの基本的な算術規則に完全に従っています。PHPのような自動的に型の変換は行わない。  
Javaにおける数値計算の過程において、演算結果の型は、演算子の両側の種類に応じて最終的に決定される。

例:  

```Java
	7/3  // return int
	(1.0 * 7)/3    // return double
	(1.0f * 7)/3   // return float
```

### メソッド呼び出し
例:  

```Java
	Map map = new HashMap();
	map.put("a", new BigDecimal("7"));
	map.put("b", new BigDecimal("3"));
	assertEquals(10, EL.eval(map, "a.add(b).intValue()"));
```

### 静的メソッド呼び出し
例:  

```Java
	assertFalse((Boolean)EL.eval("'java.lang.Boolean'@FALSE"));
	assertEquals(Boolean.TRUE, EL.eval("'java.lang.Boolean'@parseBoolean('true')"));
```

### シンプルな使い方
#### 四則演算

```Java
	System.out.println(EL.eval("3+2*5"));
	// Output:  13
```

#### 文字列操作

```Java
	System.out.println(EL.eval("'  abc  '.trim()"));
	// Output:  abc
```

#### Javaのプロパティ

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	pet.setName("GFW");
	map.put("pet", pet);
	System.out.println(EL.eval("pet.name", map));
	// Output:  GFW
```

#### メソッド呼び出し

```Java
	Map map = new HashMap();
	Pet pet = new Pet();
	map.put("pet", pet);
	EL.eval("pet.setName('XiaoBai')", map);

	System.out.println(EL.eval("pet.getName()", map));
	// Output:  XiaoBai
```

#### 配列

```Java
	Map map = new HashMap();
	map.put("x", new String[] { "A", "B", "C" });

	System.out.println(EL.eval("x[0].toLowerCase()"), map);
	// Output:  a
```

#### List

```Java
	Map map = new HashMap();
	map.put("x", Arrays.asList("A", "B", "C"));

	System.out.println(EL.eval("x.get(0).toLowerCase()", map));
	// Output:  a
```

#### Map

```Java
	Map map = new HashMap();
	map.put("map", Jsons.toJson("{x:10, y:5}"));

	System.out.println(EL.eval("map['x'] * map['y']", map));
	// Output:  50
```

#### ロジカル演算

```Java
	Map map = new HashMap();
	map.put("a",5);

	System.out.println(EL.eval("a>10", map));
	// Output:  false

	map.put("a",20);
	System.out.println(EL.eval("a>10", map));
	// Output:  true
```

#### 空またはExceptionの処理

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertTrue((Boolean)EL.eval("!!(obj.pet.name) == null", ctx));
```

#### A or B

```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.eval("!!(obj.pet.name) ||| 'cat'", map));
```

### strict モード
デフォルトでは、ELはstrictモードを使用しません（nullオブジェクトの呼び出しメソッドは例外を発生させません）。  

例：  
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	assertEquals("cat", EL.eval("obj.pet.name ||| 'cat'", map));
```

strictモードで実行すると、exceptionが発生します。  
例:  
```Java
	Map map = new HashMap();
	map.set("obj", "pet");
	ELContext ctx = new ELContext(map, true);
	assertEquals("cat", EL.eval("!!(obj.pet.name) ||| 'cat'", map));
```


### ELの性能

Panda.ELは以下のように3つのステップで、EL式の評価を行っています。  

  - EL式を解析して、suffix expression配列にする
  - suffix expression配列を解析して、operation treeにする
  - operation treeのルートノードから評価を行う

同じEL式を評価する度に上記の三つのステップを通すのは効率良くないので、最初にEL式をコンパイルして、キャッシュすれば、性能の改善ができます。  
例:  

```Java
	EL exp = new EL("a*10");  // Compile a expression and got a EL instance

	Map m = new HashMap();
	m.put("a", 10);

	System.out.println(exp.eval(m));  // Output: 100 
```

メソッドEL.eval()はthread-safeです。

EL.eval("xxx")の呼び出しは内部のWeakHashMapキャッシュからEL("xxx")を検索します。  
見つからない場合は、EL("xxx")インスタンスを生成してキャッシュに保存します。


---

 - [前: DAO →](dao_ja.md)
 - [次: INDEX →](idx_ja.md)
 - [インデックス ↑](index_ja.md)
