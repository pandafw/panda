<html>
<head>
	<title><@p.text name="title-benable"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="ui-headline">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-benable"/></li>
			<li class="active"><@p.text name="step-benable-success"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-benable-success", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="ui-toolbar"><ul>
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul></div>

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
			"sortable": false,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name": "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "gender",
			"header": a.getFieldLabel("gender"),
			"display": a.displayField("gender"),
			"format": {
				"codemap": consts.petGenderMap,
				"type": "code"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("gender")
		}, {
			"name": "birthday",
			"header": a.getFieldLabel("birthday"),
			"display": a.displayField("birthday"),
			"format": {
				"type": "date"
			},
			"sortable": false,
			"tooltip": a.getFieldTooltip("birthday")
		}, {
			"name": "amount",
			"header": a.getFieldLabel("amount"),
			"display": a.displayField("amount"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("amount")
		}, {
			"name": "price",
			"header": a.getFieldLabel("price"),
			"display": a.displayField("price"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("price")
		}, {
			"name": "shopName",
			"header": a.getFieldLabel("shopName"),
			"display": a.displayField("shopName"),
			"sortable": false,
			"tooltip": a.getFieldTooltip("shopName")
		}, {
			"name": "status",
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


	<@p.listview id="pet_benable"
		list=result columns=_columns_ cssColumn="status"
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
