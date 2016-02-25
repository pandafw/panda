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
<#if text.getText("well-bdelete", "")?has_content>
	<div class="p-well"><@p.text name="well-bdelete"/></div>
</#if>


	<#include "/action-alert.ftl"/>
	<br/>

<#if result?has_content>
	<#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": text.getText("listview-th-rownum", ""),
			"fixed": true
		}] />
<#if a.displayColumn("id")>
	<#assign _columns_ = _columns_ + [{
			"name": "id",
			"header": text.getText("a.t.id"),
			"sortable": false,
			"tooltip": text.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayColumn("name")>
	<#assign _columns_ = _columns_ + [{
			"name": "name",
			"header": text.getText("a.t.name"),
			"sortable": false,
			"tooltip": text.getText("a.t.name-tip", "")
		}] />
</#if>
<#if a.displayColumn("size")>
	<#assign _columns_ = _columns_ + [{
			"name": "size",
			"header": text.getText("a.t.size"),
			"format": {
				"type": "integer"
				},
			"sortable": false,
			"tooltip": text.getText("a.t.size-tip", "")
		}] />
</#if>
<#if a.displayColumn("date")>
	<#assign _columns_ = _columns_ + [{
			"name": "date",
			"header": text.getText("a.t.date"),
			"format": {
				"type": "timestamp"
				},
			"sortable": false,
			"tooltip": text.getText("a.t.date-tip", "")
		}] />
</#if>
<#if a.displayColumn("flag")>
	<#assign _columns_ = _columns_ + [{
			"name": "flag",
			"header": text.getText("a.t.flag"),
			"format": {
				"type": "integer"
				},
			"sortable": false,
			"tooltip": text.getText("a.t.flag-tip", "")
		}] />
</#if>


	<@p.listview id="filepool_bdelete"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter">
		
	<#if a.hasPermission("~/list")>
		<@p.a btn="default" icon="icon-list" action="~/list" label="#(button-list)"/>
	</#if>
	</div>
<#else>
	<div class="p-tcenter">
		<@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
</#if>
</div>

</body>
</html>
