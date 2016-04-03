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
<@p.head maxAge="0" charset="UTF-8" bom="true" filename="%{vars._fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
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
<#if a.displayField("size")>
	<#assign _columns_ = _columns_ + [{
			"name": "size",
			"header": a.getText("a.t.size"),
			"format": {
				"type": "integer"
			},
			"hidden": false
		}] />
</#if>
<#if a.displayField("date")>
	<#assign _columns_ = _columns_ + [{
			"name": "date",
			"header": a.getText("a.t.date"),
			"format": {
				"type": "timestamp"
			},
			"hidden": false
		}] />
</#if>
<#if a.displayField("flag")>
	<#assign _columns_ = _columns_ + [{
			"name": "flag",
			"header": a.getText("a.t.flag"),
			"format": {
				"type": "integer"
			},
			"hidden": false
		}] />
</#if>
<@p.csv list=result columns=_columns_/>
</#if>