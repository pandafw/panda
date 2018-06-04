Panda.Ioc - XML Configuration
============================

XML configuration example file: 
```Xml
<ioc>
	<tom>
		<name>Tom</name>
		<birthday>2009-10-25 15:23:40</birthday>
	</tom>
	<jerry>
		<type>panda.ioc.sample.json.Pet</type>
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
			<type>panda.ioc.sample.json.Pet</type>
			<args>
				<i>anonymous</i>
			</args>
		</friend>
	</john>
</ioc>
```

Equivalent to the following JSON configuration file:
```JavaScript
{
	tom: {
		name: 'Tom',
		birthday: '2009-10-25 15:23:40'
	},
	jerry: {
		type: 'panda.ioc.sample.json.Pet',
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
			type: 'panda.ioc.sample.json.Pet',
			args: [ 'anonymous' ],
		}
	},
}
```



---

 - [Previous: Annotation Loader →](annotation_en.md)
 - [Next: Composite Loader →](combo_en.md)
 - [Index ↑](ioc_en.md#Index)
