<html>
<head>
	<title><@p.text name="title-list_print"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-list_print"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": text.getText("listview-th-number", ""),
			"nowrap": true,
			"fixed": true
		}, {
			"name": "id",
			"value": false,
			"header": text.getText("a.t.id"),
			"sortable": false,
			"tooltip": text.getText("a.t.id-tip", "")
		}, {
			"name": "name",
			"value": false,
			"header": text.getText("a.t.name"),
			"sortable": false,
			"tooltip": text.getText("a.t.name-tip", "")
		}, {
			"name": "size",
			"value": false,
			"header": text.getText("a.t.size"),
			"format": {
				"type": "integer"
			},
			"sortable": false,
			"tooltip": text.getText("a.t.size-tip", "")
		}, {
			"name": "date",
			"value": false,
			"header": text.getText("a.t.date"),
			"format": {
				"type": "timestamp"
			},
			"sortable": false,
			"tooltip": text.getText("a.t.date-tip", "")
		}, {
			"name": "flag",
			"value": false,
			"header": text.getText("a.t.flag"),
			"format": {
				"type": "integer"
			},
			"sortable": false,
			"tooltip": text.getText("a.t.flag-tip", "")
		}
	] />

	<@p.listview id="filepool_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-striped table-striped" autosize="false" script="false"
	/>
</div>

</body>
</html>
