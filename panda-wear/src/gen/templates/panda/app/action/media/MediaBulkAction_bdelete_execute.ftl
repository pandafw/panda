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
<#if a.canAccess("~/list")><li><@p.a action="~/list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>
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
			"name": "kind",
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
			"header": a.getFieldLabel("mediaName"),
			"display": a.displayField("mediaName"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaName")
		}, {
			"name": "mediaSize",
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
			"header": a.getFieldLabel("mediaWidth"),
			"display": a.displayField("mediaWidth"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaWidth")
		}, {
			"name": "mediaHeight",
			"header": a.getFieldLabel("mediaHeight"),
			"display": a.displayField("mediaHeight"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("mediaHeight")
		}, {
			"name": "mediaFile",
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
			"hidden": true,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedBy")
		}, {
			"name": "updatedByUser",
			"header": a.getFieldLabel("updatedByUser"),
			"display": a.displayField("updatedByUser"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("updatedByUser")
		}] />


	<@p.listview id="media_bdelete"
		list=result columns=_columns_
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter" focusme="true">
	<#if a.canAccess("~/list")>
		<@p.a action="~/list" btn="default" icon="icon-list" label="#(btn-list)"/>
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
