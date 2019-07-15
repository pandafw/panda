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
			"fixed": true,
			"header": a.getText("listview-th-number", "")
		}, {
			"name": "id",
			"value": false,
			"header": a.getFieldLabel("id"),
			"display": a.displayField("id"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name": "name",
			"value": false,
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "kind",
			"value": false,
			"header": a.getFieldLabel("kind"),
			"display": a.displayField("kind"),
			"format": {
				"codemap": consts.tagKindMap,
				"type": "code"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("kind")
		}, {
			"name": "code",
			"value": false,
			"header": a.getFieldLabel("code"),
			"display": a.displayField("code"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("code")
		}] />


	<@p.listview id="tags_list_print" action="./list.print" 
		list=result.list columns=_columns_
		cssTable="table-striped"
	/>
</div>

</body>
</html>
