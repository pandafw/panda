<html>
<head>
	<title><@p.text name="title-list"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-list"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-list", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("~/add")><li><@p.a action="~/add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if a.canAccess("~/list.print")><li><@p.a action="~/list.print" includeParams="all" icon="icon-print" label="#(btn-print)" target="_blank"/>
</li></#if><#if a.canAccess("~/expo.csv")><li><@p.a action="~/expo.csv" includeParams="all" icon="icon-csv" label="#(btn-csv)" target="_blank"/>
</li></#if><#if a.canAccess("~/list.json")><li><@p.a action="~/list.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("~/list.xml")><li><@p.a action="~/list.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"fixed": true,
		"header": a.getText("listview-th-number", "")
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if a.canAccess("~/add")>
		<@p.url var='_u_' action='~/add' escapeAmp='true'/>
		<#assign _ash_ = '<a class="p-lv-ia" href="' + vars._u_ + '" title="' + a.getText('tip-new', '')?html + '"><i class="' + a.getText('icon-new', '') + '"></i>' + a.getText('lbl-new', '') + '</a>'/>
	</#if>
	<#if a.canAccess("~/copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/copy",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-copy"),
			"label": a.getText("lbl-copy", ""),
			"tooltip": a.getText("tip-copy", "")
		}] />
	</#if>
	<#if a.canAccess("~/edit")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/edit",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-edit"),
			"label": a.getText("lbl-edit", ""),
			"tooltip": a.getText("tip-edit", "")
		}] />
	</#if>
	<#if a.canAccess("~/delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/delete",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-delete"),
			"label": a.getText("lbl-delete", ""),
			"tooltip": a.getText("tip-delete", "")
		}] />
	</#if>
	<#if _actions_?has_content || _ash_?has_content>
		<#if !(_ash_?has_content)>
			<#assign _ash_ = a.getText("listview-th-actions", "")/>
		</#if>
		<#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"fixed": true,
			"header": _ash_,
			"actions": _actions_
		}] />
	</#if>
	<#if a.actionsAlignLeft>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<#if action.canAccess("~/bdelete") || action.canAccess("~/benable") || action.canAccess("~/bdisable")>
		<#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
	</#if>

	<#assign _columns_ = _columns_ + [{
			"name" : "id",
			"pkey" : true,
			"value": true,
			"header": a.getFieldLabel("id"),
			"display": a.displayField("id"),
			"filterable": a.filterField("id"),
			"filter": {
				"type": "number"
			},
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name" : "clazz",
			"header": a.getFieldLabel("clazz"),
			"display": a.displayField("clazz"),
			"filterable": a.filterField("clazz"),
			"filter": {
				"fixed": true,
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("clazz")
		}, {
			"name" : "language",
			"header": a.getFieldLabel("language"),
			"display": a.displayField("language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"filterable": a.filterField("language"),
			"filter": {
				"fixed": true,
				"list": consts.localeLanguageMap,
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("language")
		}, {
			"name" : "country",
			"header": a.getFieldLabel("country"),
			"display": a.displayField("country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"filterable": a.filterField("country"),
			"filter": {
				"fixed": true,
				"list": consts.localeCountryMap,
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("country")
		}, {
			"name" : "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"filterable": a.filterField("name"),
			"filter": {
				"fixed": true,
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name" : "value",
			"header": a.getFieldLabel("value"),
			"display": a.displayField("value"),
			"filterable": a.filterField("value"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("value")
		}, {
			"name" : "memo",
			"header": a.getFieldLabel("memo"),
			"display": a.displayField("memo"),
			"filterable": a.filterField("memo"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("memo")
		}, {
			"name" : "status",
			"header": a.getFieldLabel("status"),
			"display": a.displayField("status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"filterable": a.filterField("status"),
			"filter": {
				"list": consts.dataStatusMap,
				"type": "checklist"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("status")
		}, {
			"name" : "updatedAt",
			"header": a.getFieldLabel("updatedAt"),
			"display": a.displayField("updatedAt"),
			"format": {
				"type": "datetime"
			},
			"filterable": a.filterField("updatedAt"),
			"filter": {
				"type": "datetime"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("updatedAt")
		}, {
			"name" : "updatedBy",
			"header": a.getFieldLabel("updatedBy"),
			"display": a.displayField("updatedBy"),
			"filterable": a.filterField("updatedBy"),
			"filter": {
				"type": "number"
			},
			"hidden": true,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("updatedBy")
		}, {
			"name" : "updatedByUser",
			"header": a.getFieldLabel("updatedByUser"),
			"display": a.displayField("updatedByUser"),
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("updatedByUser")
		}] />


	<#if a.actionsAlignRight>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if a.canAccess("~/bdelete")>
			<@p.b onclick="return property_list_bdelete();" icon="icon-bdelete" label="#(btn-bdelete)"/>
		</#if>
		<#if a.canAccess("~/benable")>
			<@p.b onclick="return property_list_benable();" icon="icon-benable" label="#(btn-benable)"/>
		</#if>
		<#if a.canAccess("~/bdisable")>
			<@p.b onclick="return property_list_bdisable();" icon="icon-bdisable" label="#(btn-bdisable)"/>
		</#if>
	</@p.set>

	<@p.queryer id="property_list_qr" action="~/list"
		columns=_columns_
	/>

	<@p.listview id="property_list" action="~/list"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
		link={ "action": "~/view", "params": { "id": "%{top.id}" } }
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
		function property_list_bdelete() {
			return plv_submitCheckedKeys('property_list', '<@p.url action="~/bdelete"/>', null, "");
		}
		function property_list_benable() {
			return plv_submitCheckedKeys('property_list', '<@p.url action="~/benable"/>', null, "");
		}
		function property_list_bdisable() {
			return plv_submitCheckedKeys('property_list', '<@p.url action="~/bdisable"/>', null, "");
		}
	--></script>
</div>

</body>
</html>
