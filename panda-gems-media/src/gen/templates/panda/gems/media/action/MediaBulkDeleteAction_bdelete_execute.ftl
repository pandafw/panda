<html>
<head>
	<title><@p.text name="title-bdelete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-bdelete"/></li>
			<li class="active"><@p.text name="step-bdelete-success"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-bdelete-success", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<div id="media_alert"><#include "/action-alert.ftl"/></div>
	<br/>

<#if result?has_content>
	<#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": a.getText("listview-th-rownum", ""),
			"fixed": true
		}, {
			"name": "id",
			"header": a.getFieldLabel("id"),
			"display": a.displayField("id"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name": "slug",
			"header": a.getFieldLabel("slug"),
			"display": a.displayField("slug"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("slug")
		}, {
			"name": "tag",
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
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "size",
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
			"header": a.getFieldLabel("width"),
			"display": a.displayField("width"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("width")
		}, {
			"name": "height",
			"header": a.getFieldLabel("height"),
			"display": a.displayField("height"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("height")
		}, {
			"name": "createdAt",
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


	<@p.listview id="media_bdelete"
		list=result columns=_columns_
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter" focusme="true">
	<#if a.canAccess("./list")>
		<@p.a action="./list" btn="default" icon="icon-list" label="#(btn-list)"/>
	</#if>
	</div>
<#else>
	<div class="p-tcenter">
		<@p.a href="#" onclick="window.history.back();return false;" btn="default" icon="back" label="#(btn-back)"/>
	</div>
</#if>
</div>

</body>
</html>
