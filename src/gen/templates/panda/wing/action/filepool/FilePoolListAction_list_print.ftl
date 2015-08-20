<html>
<head>
	<title><@p.text name="title-list_print"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-list_print"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/panda/mvc/view/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "id",
		"header": text.getText("a.t.id"),
		"sortable": false,
		"tooltip": text.getText("a.t.id-tip", ""),
		"value": false			
	}, {
		"name": "name",
		"header": text.getText("a.t.name"),
		"sortable": false,
		"tooltip": text.getText("a.t.name-tip", ""),
		"value": false			
	}, {
		"name": "size",
		"header": text.getText("a.t.size"),
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": text.getText("a.t.size-tip", ""),
		"value": false			
	}, {
		"name": "date",
		"header": text.getText("a.t.date"),
		"format": {
			"type": "timestamp"
		},
		"sortable": false,
		"tooltip": text.getText("a.t.date-tip", ""),
		"value": false			
	}, {
		"name": "flag",
		"header": text.getText("a.t.flag"),
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": text.getText("a.t.flag-tip", ""),
		"value": false			
	}] />

	<@p.listview id="filepool_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		autosize="false" script="false"
	/>
</div>

</body>
</html>
