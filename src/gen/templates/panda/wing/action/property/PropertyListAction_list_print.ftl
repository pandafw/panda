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
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.id-tip", ""),
		"value": false			
	}, {
		"name": "clazz",
		"header": text.getText("a.t.clazz"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.clazz-tip", ""),
		"value": false			
	}, {
		"name": "language",
		"header": text.getText("a.t.language"),
		"format": {
			"codemap": consts.localeLanguageMap,
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.language-tip", ""),
		"value": false			
	}, {
		"name": "country",
		"header": text.getText("a.t.country"),
		"format": {
			"codemap": consts.localeCountryMap,
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.country-tip", ""),
		"value": false			
	}, {
		"name": "name",
		"header": text.getText("a.t.name"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.name-tip", ""),
		"value": false			
	}, {
		"name": "value",
		"header": text.getText("a.t.value"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.value-tip", ""),
		"value": false			
	}, {
		"name": "memo",
		"header": text.getText("a.t.memo"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.memo-tip", ""),
		"value": false			
	}, {
		"name": "status",
		"header": text.getText("a.t.status"),
		"format": {
			"codemap": consts.dataStatusMap,
			"type": "code"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.status-tip", ""),
		"value": false			
	}, {
		"name": "uusid",
		"header": text.getText("a.t.uusid"),
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.uusid-tip", ""),
		"value": false			
	}, {
		"name": "utime",
		"header": text.getText("a.t.utime"),
		"format": {
			"type": "datetime"
		},
		"hidden": false,
		"sortable": false,
		"tooltip": text.getText("a.t.utime-tip", ""),
		"value": false			
	}] />

	<@p.listview id="property_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		autosize="false" script="false"
	/>
</div>

</body>
</html>
