 Panda Framework
=================

Panda是一个Java的开发框架，它可以帮你快速的建立一个WEB应用。
对比Spring/Struts2/Hibernate这些流行的Java应用框架，Panda的特点是小而全，上手简单，开发容易。


Panda是由以下几个部分组成的。

 | 模块                   | 说明                                                                      |
 |----------------------- |---------------------------------------------------------------------------|
 | panda-core             | 这是Panda框架中最基础的部分，它包含了类似apache-commons的许多有用的类。   |
 | panda-gear             | IOC/MVC模块。类似Spring-MVC, Struts2。包含了一些常用的Taglib。            |
 | panda-html             | 包含了jQuery, Bootstrap, 以及panda-gear中一些taglib所需要的css/javascript。|
 | panda-rail             | 扩展模块。包含了实现CRUD的基础模板类，用户认证/权限管理的基础类，和一些其他的有用的类。|
 | panda-soup             | ASM/AOP模块                                                               |
 | panda-tool             | 源代码生成器。可以自动生成Entity类, Query类, Dao类, Action类, View的Freemarker(HTML)模板文件。 |
 | panda-tube             | Java XML/JSON Serializer/Deserializer                                     |



一些感想
---------------------------

Panda的最大亮点或许是panda-tool这个源代码生成器吧。

许多做WEB应用的同学都会发现，好多页面（画面）的功能不外乎“列表/新建/修改/删除”这四种。
好多代码都是重复编写，又烦又累。所以我就想开发一个源代码生成器，通过一个XML定义文件来生成我需要的代码。  

好几年前开发的最初版源代码生成器生成的源代码是基于Struts2/iBatis2的，但是有许多我想要的功能都没有办法实现。
正在那时，我看到了国内wendal大大开发的Nutz框架，牛人啊，独自开发了能够实现Spring/Hibernate功能的精巧框架。
很不好意思，我借用了Nutz的API设计，修改或自己编写了里面的实现。

断断续续坚持编写了好几年，总算完成了一个我比较满意的框架。
它可以实现Spring/Struts2/Hibernate的一些非常常用的功能。
并且可以通过XML定义文件来自动产生实现“检索/列表/新建/修改/删除”功能的源代码（包括HTML页面及后台处理）。

我做了一个[http://pdemo.foolite.com]DEMO网站，通过这个DEMO大家可以比较直观的了解Panda。


如果大家觉得panda好用,并且能够对你的工作有帮助，留个言提个建议或写个bug报告的话，我会非常感激。
欢迎Folk。


