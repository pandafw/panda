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
<#assign _well = a.getText("well-list_csv", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>

	<#include "/action-alert.ftl"/>
</div>

</body>
</html>
<#else>
<@p.text var="_fn_" name="title-list_csv" escape="none"><@p.param name="title" value="#(title)"/></@p.text>
<@p.head expiry="0" charset="UTF-8" bom="true" filename="%{vars._fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
<#assign _columns_ = [] />
<#if a.displayField("id")>
	<#assign _columns_ = _columns_ + [{
			"name": "id",
			"header": a.getText("a.t.id"),
			"hidden": false
		}] />
</#if>
<#if a.displayField("name")>
	<#assign _columns_ = _columns_ + [{
			"name": "name",
			"header": a.getText("a.t.name"),
			"hidden": false
		}] />
</#if>
<#if a.displayField("language")>
	<#assign _columns_ = _columns_ + [{
			"name": "language",
			"header": a.getText("a.t.language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"hidden": false
		}] />
</#if>
<#if a.displayField("country")>
	<#assign _columns_ = _columns_ + [{
			"name": "country",
			"header": a.getText("a.t.country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"hidden": false
		}] />
</#if>
<#if a.displayField("status")>
	<#assign _columns_ = _columns_ + [{
			"name": "status",
			"header": a.getText("a.t.status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"hidden": false
		}] />
</#if>
<#if a.displayField("uusid")>
	<#assign _columns_ = _columns_ + [{
			"name": "uusid",
			"header": a.getText("a.t.uusid"),
			"hidden": false
		}] />
</#if>
<#if a.displayField("utime")>
	<#assign _columns_ = _columns_ + [{
			"name": "utime",
			"header": a.getText("a.t.utime"),
			"format": {
				"type": "datetime"
			},
			"hidden": false
		}] />
</#if>
<@p.csv list=result columns=_columns_/>
</#if>