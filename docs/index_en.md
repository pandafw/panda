 Panda Framework
=================

Panda is a Java development framework that helps you quickly develop a web application.
Compared to popular Java application frameworks such as Spring/Struts2/Hibernate, Panda is small and complete, easy to use, and easy to develop.

Panda Framework consists of the following modules.

 | Module                         | Description                                                               |
 |--------------------------------|---------------------------------------------------------------------------|
 | [panda-core](core/index_en.md) | This is the most basic part of the Panda framework and it contains many useful classes like apache-commons.  |
 | [panda-gear](gear/index_en.md) | [IOC](gear/ioc/ioc_en.md)/[MVC](gear/mvc/mvc_en.md) module. Similar to Spring-MVC, Struts2, contains some commonly used taglib. |
 | [panda-glue](glue/index_en.md) | [ASM](glue/asm_en.md)/[AOP](glue/aop_en.md) module.                  |
 | [panda-html](html/index_en.md) | Contains jQuery, Bootstrap, and the css/javascript required by panda-gear's taglib. |
 | [panda-lane](lane/index_en.md) | Extension module. Contains basic template classes that implement CRUD, user authentication, and some other useful classes.|
 | [panda-tool](tool/index_en.md) | Source code generator. Can generate Entity/Query/Dao/Action class, Freemarker (HTML) template file. |
 | [panda-tube](tube/index_en.md) | Web Service API client (WordPress XMP-RPC, Google Vision API).                     |



Some thoughts
---------------------------

Perhaps the biggest highlight of Panda Framework is the panda-tool source code generator.

Many programmers who do web application development will find that many pages function is nothing more than "list / create / update / delete" these four patterns.
A lot of code is written repeatedly, annoying and tired. So I just want to develop a source code generator to generate the code I need through an XML definition file.

The first version source code generator developed several years ago can generate source based on Struts2/iBatis2. 
But there are many features can not be implement easily by that Struts2/iBatis2. 
At that time, I meet the Nutz framework developed by Wenda. A small compact framework that can implement the features of Spring / Hibernate.
I am sorry for borrowed some of Nutz's API design, modified it and wrote my own implementation.

Having persisted in writing for several years, I finally completed a framework that I was satisfied with. 
It can implement some very common features of Spring/Struts2/Hibernate. 
The source code generator can generate the source code (including HTML pages and server side logic) implements the CRUD features.

I made a [DEMO website](http://pandafw.ga). Through this DEMO, you may understand the Panda Framework more intuitively.

I would be very grateful for that if you think the Panda Framework is easy to use and can help your work well.
Leave a suggestion or write a bug report, welcome to Folk.

