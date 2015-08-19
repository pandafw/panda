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
	"name": "clazz",
	"header": action.getText("d.clazz"), 
	"hidden": false
}, {
	"name": "language",
	"header": action.getText("d.language"), 
	"format": {
		"codemap": "consts.localeLanguageMap",
		"type": "code"
	},
	"hidden": false
}, {
	"name": "country",
	"header": action.getText("d.country"), 
	"format": {
		"codemap": "consts.localeCountryMap",
		"type": "code"
	},
	"hidden": false
}, {
	"name": "name",
	"header": action.getText("d.name"), 
	"hidden": false
}, {
	"name": "value",
	"header": action.getText("d.value"), 
	"hidden": false
}, {
	"name": "memo",
	"header": action.getText("d.memo"), 
	"hidden": false
}, {
	"name": "status",
	"header": action.getText("d.status"), 
	"format": {
		"codemap": "consts.dataStatusMap",
		"type": "code"
	},
	"hidden": false
}, {
	"name": "uusid",
	"header": action.getText("d.uusid"), 
	"hidden": false
}, {
	"name": "utime",
	"header": action.getText("d.utime"), 
	"format": {
		"type": "datetime"
	},
	"hidden": false
}] />
<@p.csv list="ds" columns=_columns_/>
</#if>