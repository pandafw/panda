<html>
<head>
	<title><@p.text name="title-bdisable"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-bdisable"/></li>
			<li class="active"><@p.text name="step-bdisable-confirm"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-bdisable", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
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
			"name": "_check_",
			"type": "check",
			"fixed": true
		}, {
			"name": "id",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("id"),
			"display": a.displayField("id"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name": "clazz",
			"header": a.getFieldLabel("clazz"),
			"display": a.displayField("clazz"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("clazz")
		}, {
			"name": "language",
			"header": a.getFieldLabel("language"),
			"display": a.displayField("language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("language")
		}, {
			"name": "country",
			"header": a.getFieldLabel("country"),
			"display": a.displayField("country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("country")
		}, {
			"name": "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name": "value",
			"header": a.getFieldLabel("value"),
			"display": a.displayField("value"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("value")
		}, {
			"name": "memo",
			"header": a.getFieldLabel("memo"),
			"display": a.displayField("memo"),
			"hidden": false,
			"sortable": false,
			"tooltip": a.getFieldTooltip("memo")
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


	<@p.listview id="property_bdisable"
		action="./bdisable.execute" method="post"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
	/>
	
	<br/>
	<div class="p-tcenter" focusme="true">
		<@p.submit onclick="return property_bdisable_submit();" icon="icon-bdisable-execute" label="#(btn-bdisable-execute)"/>

		<@p.a href="javascript:window.history.back()" btn="default" icon="icon-back" label="#(btn-back)"/>

		<script type="text/javascript"><!--
			function property_bdisable_submit() {
				return plv_submitCheckedKeys('property_bdisable');
			}
			
			function onPageLoad() {
				plv_checkAll('property_bdisable');
			}
		--></script>
	</div>
<#else>
	<div class="p-tcenter">
		<@p.a href="#" onclick="window.history.back();return false;" btn="default" icon="back" label="#(btn-back)"/>
	</div>
</#if>
</div>

</body>
</html>
