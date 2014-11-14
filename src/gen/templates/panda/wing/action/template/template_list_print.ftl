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
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.id-tip", ""),
		"value": false			
	}, {
		"name": "name",
		"header": action.getText("d.name", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.name-tip", ""),
		"value": false			
	}, {
		"name": "language",
		"header": action.getText("d.language", ""), 
		"format": {
			"codemap": "consts.localeLanguageMap",
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.language-tip", ""),
		"value": false			
	}, {
		"name": "country",
		"header": action.getText("d.country", ""), 
		"format": {
			"codemap": "consts.localeCountryMap",
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.country-tip", ""),
		"value": false			
	}, {
		"name": "source",
		"header": action.getText("d.source", ""), 
		"hidden": true,
		"sortable": false,
		"tooltip": action.getText("d.source-tip", ""),
		"value": false			
	}, {
		"name": "status",
		"header": action.getText("d.status", ""), 
		"format": {
			"codemap": "consts.dataStatusMap",
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.status-tip", ""),
		"value": false			
	}, {
		"name": "uusid",
		"header": action.getText("d.uusid", ""), 
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.uusid-tip", ""),
		"value": false			
	}, {
		"name": "utime",
		"header": action.getText("d.utime", ""), 
		"format": {
			"type": "datetime"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": action.getText("d.utime-tip", ""),
		"value": false			
	}] />

	<@p.listview id="template_list_print" action="template_list_print" 
		list="ds" columns=_columns_ cssColumn="status"
		start="pg.s" limit="pg.l" total="pg.t" sort="so.c" dir="so.d"
		autosize="false" script="false"
	/>
</div>

</body>
</html>
