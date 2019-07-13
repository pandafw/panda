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
			"name": "thumbnail",
			"value": false,
			"header": a.getFieldLabel("thumbnail"),
			"display": a.displayField("thumbnail"),
			"format": {
				"expr": "<img class='p-mwh32px' src='%{!!(b.media_path)|||'/media'}/icon/%{top.thumbnail}'>",
				"escape": "none",
				"type": "tran"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("thumbnail")
		}, {
			"name": "slug",
			"value": false,
			"header": a.getFieldLabel("slug"),
			"display": a.displayField("slug"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("slug")
		}, {
			"name": "title",
			"value": false,
			"header": a.getFieldLabel("title"),
			"display": a.displayField("title"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("title")
		}, {
			"name": "tag",
			"value": false,
			"header": a.getFieldLabel("tag"),
			"display": a.displayField("tag"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("tag")
		}, {
			"name": "publishDate",
			"value": false,
			"header": a.getFieldLabel("publishDate"),
			"display": a.displayField("publishDate"),
			"format": {
				"type": "datetime"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("publishDate")
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
			"format": {
				"expr": "top.createdByUser",
				"type": "expr"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("createdBy")
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
			"format": {
				"expr": "top.updatedByUser",
				"type": "expr"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedBy")
		}] />


	<@p.listview id="pages_list_print" action="./list.print" 
		list=result.list columns=_columns_ cssColumn="status"
		cssTable="table-striped"
	/>
</div>

</body>
</html>
