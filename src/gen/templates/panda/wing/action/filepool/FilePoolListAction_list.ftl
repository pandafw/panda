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
<#if text.getText("well-list", "")?has_content>
	<div class="p-well"><@p.text name="well-list"/></div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if action.hasPermission("~/add")><li><@p.a icon="icon-new" action="~/add" label="#(button-new)"/>
</li></#if><#if action.hasPermission("~/list_print")><li><@p.a icon="icon-print" target="_blank" action="~/list_print" includeParams="all" label="#(button-print)"/>
</li></#if><#if action.hasPermission("~/list_csv")><li><@p.a icon="icon-csv" target="_blank" action="~/list_csv" includeParams="all" label="#(button-csv)"/>
</li></#if><#if action.hasPermission("~/list_json")><li><@p.a icon="icon-json" target="_blank" action="~/list_json" includeParams="all" label="#(button-json)"/>
</li></#if><#if action.hasPermission("~/list_xml")><li><@p.a icon="icon-xml" target="_blank" action="~/list_xml" includeParams="all" label="#(button-xml)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": text.getText("listview-th-number", ""),
		"fixed": true
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if action.hasPermission("~/add")>
		<@p.url var='_u_' action='~/add'/>
		<#assign _ash_ = '<a class="n-lv-ia" href="' + vars._u_ + '" title="' + text.getText('tooltip-new', '')?html + '"><i class="' + text.getText('icon-new', '') + '"></i>' + text.getText('label-new', '') + '</a>'/>
	</#if>
	<#if action.hasPermission("~/copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/copy",
			"params": { "id": "%{top.id}" },
			"icon": text.getText("icon-copy"),
			"label": text.getText("label-copy", ""),
			"tooltip": text.getText("tooltip-copy", "")
		}] />
	</#if>
	<#if action.hasPermission("~/edit")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/edit",
			"params": { "id": "%{top.id}" },
			"icon": text.getText("icon-edit"),
			"label": text.getText("label-edit", ""),
			"tooltip": text.getText("tooltip-edit", "")
		}] />
	</#if>
	<#if action.hasPermission("~/delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/delete",
			"params": { "id": "%{top.id}" },
			"icon": text.getText("icon-delete"),
			"label": text.getText("label-delete", ""),
			"tooltip": text.getText("tooltip-delete", "")
		}] />
	</#if>
	<#if _actions_?has_content || _ash_?has_content>
		<#if !(_ash_?has_content)>
			<#assign _ash_ = text.getText("listview-th-actions", "")/>
		</#if>
		<#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"header": _ash_,
			"actions": _actions_,
			"fixed": true
		}] />
	</#if>
	<#if a.actionsAlignLeft>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<#if action.hasPermission("~/bdelete")>
		<#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"fixed": true
		}] />
	</#if>

<#if a.displayColumn("id")>
	<#assign _columns_ = _columns_ + [{
			"name" : "id",
			"pkey" : true,
			"value": true,
			"header": text.getText("a.t.id"),
			"filter": {
				"type": "number"
			},
			"link": true,
			"sortable": true,
			"tooltip": text.getText("a.t.id-tip", "")
		}] />
</#if>
<#if a.displayColumn("name")>
	<#assign _columns_ = _columns_ + [{
			"name" : "name",
			"header": text.getText("a.t.name"),
			"filter": {
				"type": "string"
			},
			"sortable": true,
			"tooltip": text.getText("a.t.name-tip", "")
		}] />
</#if>
<#if a.displayColumn("size")>
	<#assign _columns_ = _columns_ + [{
			"name" : "size",
			"header": text.getText("a.t.size"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": text.getText("a.t.size-tip", "")
		}] />
</#if>
<#if a.displayColumn("date")>
	<#assign _columns_ = _columns_ + [{
			"name" : "date",
			"header": text.getText("a.t.date"),
			"format": {
				"type": "timestamp"
			},
			"filter": {
				"type": "datetime"
			},
			"sortable": true,
			"tooltip": text.getText("a.t.date-tip", "")
		}] />
</#if>
<#if a.displayColumn("flag")>
	<#assign _columns_ = _columns_ + [{
			"name" : "flag",
			"header": text.getText("a.t.flag"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": text.getText("a.t.flag-tip", "")
		}] />
</#if>

	<#if a.actionsAlignRight>
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if action.hasPermission("~/bdelete")>
			<@p.b icon="icon-bdelete" onclick="return filepool_list_bdelete();" label="#(button-bdelete)"/>
		</#if>
	</@p.set>

	<@p.listview id="filepool_list" action="~/list" 
		list=result columns=_columns_ cssColumn="status"
		cssTable="table-hover table-striped"
		link={ "action": "~/view", "params": { "id": "%{top.id}" } }
		tools="%{vars.lvtools}"
	/>

	<script type="text/javascript"><!--
		function filepool_list_bdelete() {
			return plv_submitCheckedKeys('filepool_list', '<@p.url action="~/bdelete"/>', null, "");
		}
	--></script>
</div>

</body>
</html>
