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

	<div id="fileitem_alert"><#include "/action-alert.ftl"/></div>
	<br/>

<#if result?has_content>
	<#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": a.getText("listview-th-rownum", ""),
			"fixed": true
		}, {
			"name": "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "size",
			"header": a.getFieldLabel("size"),
			"display": a.displayField("size"),
			"format": {
				"type": "size"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("size")
		}, {
			"name": "date",
			"header": a.getFieldLabel("date"),
			"display": a.displayField("date"),
			"format": {
				"type": "timestamp"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("date")
		}] />


	<@p.listview id="fileitem_bdelete"
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
