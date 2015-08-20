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

	<#include "/panda/mvc/view/action-alert.ftl"/>
</div>

</body>
</html>
<#else>
<@p.text var="_fn_" name="title-list_csv"><@p.param name="title" value="#(title)"/></@p.text>
<@p.head expiry="0" charset="UTF-8" bom="true" filename="%{vars._fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
<#assign _columns_ = [{
	"name": "id",
	"header": text.getText("a.t.id"),
	"hidden": false
}, {
	"name": "name",
	"header": text.getText("a.t.name"),
	"hidden": false
}, {
	"name": "size",
	"header": text.getText("a.t.size"),
	"format": {
		"type": "integer"
	},
	"hidden": false
}, {
	"name": "date",
	"header": text.getText("a.t.date"),
	"format": {
		"type": "timestamp"
	},
	"hidden": false
}, {
	"name": "flag",
	"header": text.getText("a.t.flag"),
	"format": {
		"type": "integer"
	},
	"hidden": false
}] />
<@p.csv list=result columns=_columns_/>
</#if>