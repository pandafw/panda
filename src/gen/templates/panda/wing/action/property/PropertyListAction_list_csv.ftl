<#if actionErrors?has_content>
<html>
<head>
	<title><@p.text name="title-list_csv"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-list_csv"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/action-alert.ftl"/>
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
		"name": "clazz",
		"header": text.getText("a.t.clazz"),
		"hidden": false
	}, {
		"name": "language",
		"header": text.getText("a.t.language"),
		"format": {
			"codemap": consts.localeLanguageMap,
			"type": "code"
		},
		"hidden": false
	}, {
		"name": "country",
		"header": text.getText("a.t.country"),
		"format": {
			"codemap": consts.localeCountryMap,
			"type": "code"
		},
		"hidden": false
	}, {
		"name": "name",
		"header": text.getText("a.t.name"),
		"hidden": false
	}, {
		"name": "value",
		"header": text.getText("a.t.value"),
		"hidden": false
	}, {
		"name": "memo",
		"header": text.getText("a.t.memo"),
		"hidden": false
	}, {
		"name": "status",
		"header": text.getText("a.t.status"),
		"format": {
			"codemap": consts.dataStatusMap,
			"type": "code"
		},
		"hidden": false
	}, {
		"name": "uusid",
		"header": text.getText("a.t.uusid"),
		"hidden": false
	}, {
		"name": "utime",
		"header": text.getText("a.t.utime"),
		"format": {
			"type": "datetime"
		},
		"hidden": false
	}
] />
<@p.csv list=result columns=_columns_/>
</#if>