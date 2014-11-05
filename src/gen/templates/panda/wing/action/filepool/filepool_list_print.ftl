<html>
<head>
	<title><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "id",
		"header": action.getText("d.id", ""), 
		"sortable": false,
		"tooltip": action.getText("d.id-tip", ""),
		"value": false			
	}, {
		"name": "name",
		"header": action.getText("d.name", ""), 
		"sortable": false,
		"tooltip": action.getText("d.name-tip", ""),
		"value": false			
	}, {
		"name": "size",
		"header": action.getText("d.size", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("d.size-tip", ""),
		"value": false			
	}, {
		"name": "date",
		"header": action.getText("d.date", ""), 
		"format": {
			"type": "timestamp"
		},
		"sortable": false,
		"tooltip": action.getText("d.date-tip", ""),
		"value": false			
	}, {
		"name": "flag",
		"header": action.getText("d.flag", ""), 
		"format": {
			"type": "integer"
		},
		"sortable": false,
		"tooltip": action.getText("d.flag-tip", ""),
		"value": false			
	}] />

	<@p.listview id="filepool_list_print" action="filepool_list_print" 
		list="ds" columns=_columns_
		start="pg.s" limit="pg.l" total="pg.t" sort="so.c" dir="so.d"
		autosize="false" script="false"
	/>
</div>

</body>
</html>
