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
<#if a.canAccess("./add")><li><@p.a action="./add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if a.canAccess("./import")><li><@p.a action="./import" icon="icon-import" label="#(btn-import)"/>
</li></#if><#if a.canAccess("./expo.csv")><li><@p.a action="./expo.csv" includeParams="all" icon="icon-csv" label="#(btn-csv)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.json")><li><@p.a action="./expo.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./expo.xml")><li><@p.a action="./expo.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.json")><li><@p.a action="./list.json" includeParams="all" icon="icon-json" label="#(btn-json)" target="_blank"/>
</li></#if><#if a.canAccess("./list.xml")><li><@p.a action="./list.xml" includeParams="all" icon="icon-xml" label="#(btn-xml)" target="_blank"/>
</li></#if><#if a.canAccess("./list.print")><li><@p.a action="./list.print" includeParams="all" icon="icon-print" label="#(btn-print)" target="_blank"/>
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
	<#if a.canAccess("./add")>
		<@p.url var='_u_' action='./add' escapeAmp='true'/>
		<#assign _ash_ = '<a class="p-lv-ia" href="' + vars._u_ + '" title="' + a.getText('tip-new', '')?html + '"><i class="' + a.getText('icon-new', '') + '"></i>' + a.getText('lbl-new', '') + '</a>'/>
	</#if>
	<#if a.canAccess("./copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "./copy",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-copy"),
			"label": a.getText("lbl-copy", ""),
			"tooltip": a.getText("tip-copy", "")
		}] />
	</#if>
	<#if a.canAccess("./edit")>
		<#assign _actions_ = _actions_ + [{
			"action": "./edit",
			"params": { "id": "%{top.id}" },
			"icon": a.getText("icon-edit"),
			"label": a.getText("lbl-edit", ""),
			"tooltip": a.getText("tip-edit", "")
		}] />
	</#if>
	<#if a.canAccess("./delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "./delete",
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

	<#if action.canAccess("./bdelete") || action.canAccess("./benable") || action.canAccess("./bdisable")>
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
			"filter": {
				"type": "number",
				"enable": a.filterField("id")
			},
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": a.getFieldTooltip("id")
		}, {
			"name" : "name",
			"header": a.getFieldLabel("name"),
			"display": a.displayField("name"),
			"filter": {
				"type": "string",
				"fixed": true,
				"enable": a.filterField("name")
			},
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": a.getFieldTooltip("name")
		}, {
			"name" : "locale",
			"header": a.getFieldLabel("locale"),
			"display": a.displayField("locale"),
			"format": {
				"codemap": consts.appLocaleMap,
				"type": "code"
			},
			"filter": {
				"type": "select",
				"fixed": true,
				"list": consts.appLocaleMap,
				"enable": a.filterField("locale")
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("locale")
		}, {
			"name" : "status",
			"header": a.getFieldLabel("status"),
			"display": a.displayField("status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"filter": {
				"type": "checklist",
				"list": consts.dataStatusMap,
				"enable": a.filterField("status")
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
			"filter": {
				"type": "datetime",
				"enable": a.filterField("updatedAt")
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("updatedAt")
		}, {
			"name" : "updatedBy",
			"header": a.getFieldLabel("updatedBy"),
			"display": a.displayField("updatedBy"),
			"format": {
				"expr": "top.updatedByUser",
				"type": "expr"
			},
			"filter": {
				"type": "number",
				"enable": a.filterField("updatedBy")
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": a.getFieldTooltip("updatedBy")
		}] />


	<#if a.actionsAlignRight>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if a.canAccess("./bdelete")>
			<@p.b onclick="return template_list_bdelete();" icon="icon-bdelete" label="#(btn-bdelete)"/>
		</#if>
		<#if a.canAccess("./benable")>
			<@p.b onclick="return template_list_benable();" icon="icon-benable" label="#(btn-benable)"/>
		</#if>
		<#if a.canAccess("./bdisable")>
			<@p.b onclick="return template_list_bdisable();" icon="icon-bdisable" label="#(btn-bdisable)"/>
		</#if>
	</@p.set>

	<@p.queryer id="template_list_qr" action="./list"
		columns=_columns_
	/>

	<@p.listview id="template_list" action="./list"
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
		link={ "action": "./view", "params": { "id": "%{top.id}" } }
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
		function template_list_bdelete() {
			return plv_submitCheckedKeys('template_list', '<@p.url action="./bdelete"/>', null, "");
		}
		function template_list_benable() {
			return plv_submitCheckedKeys('template_list', '<@p.url action="./benable"/>', null, "");
		}
		function template_list_bdisable() {
			return plv_submitCheckedKeys('template_list', '<@p.url action="./bdisable"/>', null, "");
		}
	--></script>
</div>

</body>
</html>
