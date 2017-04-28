 Panda Framework
=================

Panda is a Java development framework, it can help you quickly create a WEB application.

 - [English Readme](Readme)



Panda是一个Java的开发框架，它可以帮你快速的建立一个WEB应用。

 - [中文说明](Readme_zh)



## Thanks
 - [Apache Commons Lang](https://commons.apache.org/commons-lang/)
 - [Apache Commons Codec](https://commons.apache.org/commons-codec/)
 - [Apache Commons Collections](https://commons.apache.org/collections/)
 - [Apache Commons Net](https://commons.apache.org/net/)
 - [Struts2](https://struts.apache.org/2.x/)
 - [Nutz](https://github.com/nutzam/nutz)
 - [txtmark](https://github.com/rjeschke/txtmark)
 - [markdown4j](https://code.google.com/archive/p/markdown4j)



## History
 - 1.2.0 （2016-01-10）
   Initial Release.
   

## Requirements
 - Java 7


## Ivy Settings
    <ivysettings>
        <settings defaultResolver="default" />
        <resolvers>
            <chain name="public">
                <ibiblio name="maven" m2compatible="true" />
                <ibiblio name="panda" m2compatible="true" root="https://raw.github.com/pandafw/panda-repo/master/" />
            </chain>
        </resolvers>
        <include url="${ivy.default.settings.dir}/ivysettings-shared.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-local.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-main-chain.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-default-chain.xml" />
    </ivysettings>

