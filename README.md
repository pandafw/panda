 Panda Framework
=================

Panda is a Java development framework, it can help you quickly create a WEB application.

 - [Project Site](http://foolite.github.io/panda)
 - [English Readme](https://github.com/foolite/panda/wiki/Readme)



Panda是一个Java的开发框架，它可以帮你快速的建立一个WEB应用。

看到这里可能有的同学会说，现在开发Java的WEB应用大家都用Spring/Struts2/Hibernate这些经典的框架，
谁稀罕你这个小众框架呢。（鼓掌。。。）这位同学你说的很有道理。我最初也没有想过要做一个框架，我只想做一个
源代码生成器。

许多做WEB应用的同学都会发现，好多页面（画面）的功能不外乎“列表/新建/修改/删除”这四种。
好多代码都是重复编写，又烦又累。所以我就想开发一个源代码生成器，通过一个XML定义文件来生成我需要的代码。  

好几年前开发的最初版源代码生成器生成的源代码是基于Struts2/iBatis2的，但是有许多我想要的功能都没有办法实现。
正在那时，我看到了国内wendal大大开发的Nutz框架，牛人啊，独自开发了能够实现Spring/Hibernate功能的精巧框架。
很不好意思，我开始抄袭Nutz，盗用了Nutz的API设计，修改或自己编写了里面的实现。

断断续续坚持编写了好几年，总算完成了一个我比较满意的框架。
它可以实现Spring/Struts2/Hibernate的一些非常常用的功能。
并且可以通过XML定义文件来自动产生实现“检索/列表/新建/修改/删除”功能的源代码（包括HTML页面及后台处理）。

我做了一个[http://pdemo.foolite.com]DEMO网站，通过这个DEMO大家可以比较直观的了解Panda。

感谢同学能够耐心的看完上面那一堆啰嗦的废话，下面言归正传。
Panda是由以下几个部分组成的。


 - panda-core

 这是Panda框架中最基础的部分，它包含了类似apache-commons的许多有用的类,类似Spring的IOC/AOP。


 - panda-gear

 MVC模块。类似Spring-MVC, Struts2。包含了一些常用的Taglib。

 
 - panda-wing

 扩展模块。包含了实现CRUD的基础模板类，用户认证/权限管理的基础类，和一些其他的有用的类。
 

 - panda-html

 包含了jQuery, Bootstrap, 以及panda-gear中一些taglib所需要的css/javascript。

 
 - panda-tool
 
 源代码生成器。可以自动生成Entity类, Query类, Dao类, Action类, View的Freemarker(HTML)模板文件。
 
 
 - panda-demo
 
 panda框架的DEMO WEB应用。
 
 

 感想
-------------
如果大家觉得panda好用,并且能够对你的工作有帮助，留个言提个建议或写个bug报告的话，我会非常感激。
欢迎Folk。



 感谢
-----------
 - apache commons lang
 - apache commons codec
 - apache commons collections
 - struts2
 - Nutz
 - https://github.com/rjeschke/txtmark
 - https://code.google.com/archive/p/markdown4j



 版本历史
-------------

 - 1.2.0 （2016-01-10）
   初版。  
   （其实1.2.0之前还有好几个版本，bug多多，就不在这里写了）
   

 Requirements
--------------

 - Java 7


 - [项目网站](http://foolite.github.io/panda)
 - [中文说明](https://github.com/foolite/panda/wiki/Readme_zh)


