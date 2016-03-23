<html>
<head>
	<title><@p.text name="title-list_print"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-list_print"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-list_print", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": a.getText("listview-th-number", ""),
			"fixed": true
		}] />
<#if a.displayField("id")>
	<#assign _columns_ = _columns_ + [{
			"name": "id",
			"value": false,
			"header": a.getText("a.t.id"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayField("clazz")>
	<#assign _columns_ = _columns_ + [{
			"name": "clazz",
			"value": false,
			"header": a.getText("a.t.clazz"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.clazz-tip", "")
		}] />
</#if>
<#if a.displayField("language")>
	<#assign _columns_ = _columns_ + [{
			"name": "language",
			"value": false,
			"header": a.getText("a.t.language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.language-tip", "")
		}] />
</#if>
<#if a.displayField("country")>
	<#assign _columns_ = _columns_ + [{
			"name": "country",
			"value": false,
			"header": a.getText("a.t.country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.country-tip", "")
		}] />
</#if>
<#if a.displayField("source")>
	<#assign _columns_ = _columns_ + [{
			"name": "source",
			"value": false,
			"header": a.getText("a.t.source"),
			"hidden": true,
			"sortable": false,
			"tooltip": a.getText("a.t.source-tip", "")
		}] />
</#if>
<#if a.displayField("status")>
	<#assign _columns_ = _columns_ + [{
			"name": "status",
			"value": false,
			"header": a.getText("a.t.status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.status-tip", "")
		}] />
</#if>
<#if a.displayField("uusid")>
	<#assign _columns_ = _columns_ + [{
			"name": "uusid",
			"value": false,
			"header": a.getText("a.t.uusid"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.uusid-tip", "")
		}] />
</#if>
<#if a.displayField("utime")>
	<#assign _columns_ = _columns_ + [{
			"name": "utime",
			"value": false,
			"header": a.getText("a.t.utime"),
			"format": {
				"type": "datetime"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getText("a.t.utime-tip", "")
		}] />
</#if>


	<@p.listview id="resource_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-striped" autosize="false" script="false"
	/>
</div>

</body>
</html>
