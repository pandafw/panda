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
			"name": "kind",
			"value": false,
			"header": a.getFieldLabel("kind"),
			"display": a.displayField("kind"),
			"format": {
				"codemap": consts.mediaKindMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("kind")
		}, {
			"name": "mediaName",
			"value": false,
			"header": a.getFieldLabel("mediaName"),
			"display": a.displayField("mediaName"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaName")
		}, {
			"name": "mediaSize",
			"value": false,
			"header": a.getFieldLabel("mediaSize"),
			"display": a.displayField("mediaSize"),
			"format": {
				"type": "filesize"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaSize")
		}, {
			"name": "mediaWidth",
			"value": false,
			"header": a.getFieldLabel("mediaWidth"),
			"display": a.displayField("mediaWidth"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaWidth")
		}, {
			"name": "mediaHeight",
			"value": false,
			"header": a.getFieldLabel("mediaHeight"),
			"display": a.displayField("mediaHeight"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaHeight")
		}, {
			"name": "mediaFile",
			"value": false,
			"header": a.getFieldLabel("mediaFile"),
			"display": a.displayField("mediaFile"),
			"format": {
				"expr": "action.getMediaLink(top)",
				"escape": "none",
				"type": "expr"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaFile")
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


	<@p.listview id="media_list_print" action="~/list.print" 
		list=result columns=_columns_
		cssTable="table-striped"
	/>
</div>

</body>
</html>
