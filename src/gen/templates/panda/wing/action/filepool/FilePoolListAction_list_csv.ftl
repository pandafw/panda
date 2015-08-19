<#if action.hasErrors()>
<html>
<head>
	<title><@p.text name="title-list_csv"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-list_csv"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/panda/exts/struts2/views/action-alert-all.ftl"/>
</div>

</body>
</html>
<#else>
<@p.text var="_fn_" name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text>
<@p.head expiry="0" charset="UTF-8" bom="true" filename="%{#_fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
<#assign _columns_ = [{
	"name": "id",
	"header": action.getText("d.id"), 
	"hidden": false
}, {
	"name": "name",
	"header": action.getText("d.name"), 
	"hidden": false
}, {
	"name": "size",
	"header": action.getText("d.size"), 
	"format": {
		"type": "integer"
	},
	"hidden": false
}, {
	"name": "date",
	"header": action.getText("d.date"), 
	"format": {
		"type": "timestamp"
	},
	"hidden": false
}, {
	"name": "flag",
	"header": action.getText("d.flag"), 
	"format": {
		"type": "integer"
	},
	"hidden": false
}] />
<@p.csv list="ds" columns=_columns_/>
</#if>