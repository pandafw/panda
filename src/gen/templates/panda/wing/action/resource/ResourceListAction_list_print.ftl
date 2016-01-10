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
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.id-tip", "")
		}, {
			"name": "clazz",
			"value": false,
			"header": text.getText("a.t.clazz"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.clazz-tip", "")
		}, {
			"name": "language",
			"value": false,
			"header": text.getText("a.t.language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.language-tip", "")
		}, {
			"name": "country",
			"value": false,
			"header": text.getText("a.t.country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.country-tip", "")
		}, {
			"name": "source",
			"value": false,
			"header": text.getText("a.t.source"),
			"hidden": true,
			"sortable": false,
			"tooltip": text.getText("a.t.source-tip", "")
		}, {
			"name": "status",
			"value": false,
			"header": text.getText("a.t.status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.status-tip", "")
		}, {
			"name": "uusid",
			"value": false,
			"header": text.getText("a.t.uusid"),
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.uusid-tip", "")
		}, {
			"name": "utime",
			"value": false,
			"header": text.getText("a.t.utime"),
			"format": {
				"type": "datetime"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": text.getText("a.t.utime-tip", "")
		}
	] />

	<@p.listview id="resource_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-striped table-striped" autosize="false" script="false"
	/>
</div>

</body>
</html>
