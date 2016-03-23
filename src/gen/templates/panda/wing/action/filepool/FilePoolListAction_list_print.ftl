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
			"sortable": false,
			"tooltip": a.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayField("name")>
	<#assign _columns_ = _columns_ + [{
			"name": "name",
			"value": false,
			"header": a.getText("a.t.name"),
			"sortable": false,
			"tooltip": a.getText("a.t.name-tip", "")
		}] />
</#if>
<#if a.displayField("size")>
	<#assign _columns_ = _columns_ + [{
			"name": "size",
			"value": false,
			"header": a.getText("a.t.size"),
			"format": {
				"type": "integer"
			},
			"sortable": false,
			"tooltip": a.getText("a.t.size-tip", "")
		}] />
</#if>
<#if a.displayField("date")>
	<#assign _columns_ = _columns_ + [{
			"name": "date",
			"value": false,
			"header": a.getText("a.t.date"),
			"format": {
				"type": "timestamp"
			},
			"sortable": false,
			"tooltip": a.getText("a.t.date-tip", "")
		}] />
</#if>
<#if a.displayField("flag")>
	<#assign _columns_ = _columns_ + [{
			"name": "flag",
			"value": false,
			"header": a.getText("a.t.flag"),
			"format": {
				"type": "integer"
			},
			"sortable": false,
			"tooltip": a.getText("a.t.flag-tip", "")
		}] />
</#if>


	<@p.listview id="filepool_list_print" action="~/list_print" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-striped" autosize="false" script="false"
	/>
</div>

</body>
</html>
