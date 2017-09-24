Panda.Ioc - XML配置文件
============================

XML配置文件示例：
```Xml
<ioc>
    <tom>
        <name>Tom</name>
        <birthday>2009-10-25 15:23:40</birthday>
    </tom>
    <jerry>
        <type>panda.demo.ioc.json.Pet</type>
        <singleton>false</singleton>
        <args>
            <i>Jerry</i>
            <i>Alice</i>
        </args>
        <fields>
            <birthday>2009-11-3 08:02:14</birthday>
            <friend>#tom</friend>
        </fields>
    </jerry>
    <john>
        <name>John</name>
        <friend>
            <type>panda.demo.ioc.json.Pet</type>
            <args>
                <i>anonymous</i>
            </args>
        </friend>
    </john>
</ioc>
```

等同于以下JSON配置文件：
```JavaScript
{
    tom: {
        name: 'Tom',
        birthday: '2009-10-25 15:23:40'
    },
    jerry: {
        type: 'panda.demo.ioc.json.Pet',
        singleton: false,
        args: [ 'Jerry', 'Alice' ],
        fields: {
            birthday : '2009-11-3 08:02:14',
            friend : '#tom'
        }
    },
    john: {
        name: 'John',
        friend: {
	        type: 'panda.demo.ioc.json.Pet',
	        args: [ 'anonymous' ],
        }
    },
}
```



---

 * [上一篇：Annotation加载器 →](annotation_zh.md)
 * [下一篇：复合加载器 →](combo_zh.md)
 * [返回目录 ↑](ioc_zh.md#文档目录)
