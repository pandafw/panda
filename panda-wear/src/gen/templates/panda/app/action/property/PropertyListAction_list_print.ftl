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
			"name": "clazz",
			"value": false,
			"header": a.getFieldLabel("clazz"),
			"display": a.displayField("clazz"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("clazz")
		}, {
			"name": "locale",
			"value": false,
			"header": a.getFieldLabel("locale"),
			"display": a.displayField("locale"),
			"format": {
				"codemap": consts.appLocaleMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("locale")
		}, {
			"name": "name",
			"value": false,
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "value",
			"value": false,
			"header": a.getFieldLabel("value"),
			"display": a.displayField("value"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("value")
		}, {
			"name": "memo",
			"value": false,
			"header": a.getFieldLabel("memo"),
			"display": a.displayField("memo"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("memo")
		}, {
			"name": "status",
			"value": false,
			"header": a.getFieldLabel("status"),
			"display": a.displayField("status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("status")
		}, {
			"name": "updatedAt",
			"value": false,
			"header": a.getFieldLabel("updatedAt"),
			"display": a.displayField("updatedAt"),
			"format": {
				"type": "datetime"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedAt")
		}, {
			"name": "updatedBy",
			"value": false,
			"header": a.getFieldLabel("updatedBy"),
			"display": a.displayField("updatedBy"),
			"hidden": true,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedBy")
		}, {
			"name": "updatedByUser",
			"value": false,
			"header": a.getFieldLabel("updatedByUser"),
			"display": a.displayField("updatedByUser"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedByUser")
		}] />


	<@p.listview id="property_list_print" action="./list.print" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-striped"
	/>
</div>

</body>
</html>
