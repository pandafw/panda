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
			"name": "tag",
			"value": false,
			"header": a.getFieldLabel("tag"),
			"display": a.displayField("tag"),
			"format": {
				"codemap": consts.mediaTagMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("tag")
		}, {
			"name": "file",
			"value": false,
			"header": a.getFieldLabel("file"),
			"display": a.displayField("file"),
			"format": {
				"expr": "action.getMediaLink(top)",
				"escape": "none",
				"type": "expr"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("file")
		}, {
			"name": "name",
			"value": false,
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "size",
			"value": false,
			"header": a.getFieldLabel("size"),
			"display": a.displayField("size"),
			"format": {
				"type": "size"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("size")
		}, {
			"name": "width",
			"value": false,
			"header": a.getFieldLabel("width"),
			"display": a.displayField("width"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("width")
		}, {
			"name": "height",
			"value": false,
			"header": a.getFieldLabel("height"),
			"display": a.displayField("height"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("height")
		}, {
			"name": "createdAt",
			"value": false,
			"header": a.getFieldLabel("createdAt"),
			"display": a.displayField("createdAt"),
			"format": {
				"type": "datetime"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("createdAt")
		}, {
			"name": "createdBy",
			"value": false,
			"header": a.getFieldLabel("createdBy"),
			"display": a.displayField("createdBy"),
			"hidden": true,
			"sortable": false,
			"tooltip": a.getFieldTooltip("createdBy")
		}, {
			"name": "createdByUser",
			"value": false,
			"header": a.getFieldLabel("createdByUser"),
			"display": a.displayField("createdByUser"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("createdByUser")
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


	<@p.listview id="media_list_print" action="./list.print" 
		list=result columns=_columns_
		cssTable="table-striped"
	/>
</div>

</body>
</html>
