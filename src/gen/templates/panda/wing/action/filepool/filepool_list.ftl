<html>
<head>
	<title><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<#include "/panda/mvc/view/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": text.getText("_number_", ""),
		"nowrap": true,
		"fixed": true
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if action.hasPermission("filepool_insert")>
		<@p.url var='_u_' action='filepool_insert'/>
		<@p.text var='_icon_' name='icon-insert'/>
		<#assign _ash_ = '<a class="n-lv-ia" href="' + _u_ + '" title="' + text.getText("tooltip-insert", "")?html + '">'/>
		<#if _icon_?has_content>
			<#assign _ash_ = _ash_ + '<i class="fa ' + _icon_ + '"></i>'/>
		</#if>
		<#assign _ash_ = _ash_ + text.getText("label-insert", "") + '</a>'/>
	</#if>
	<#if action.hasPermission("filepool_copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "filepool_copy",
			"icon": text.getText("icon-copy"),
			"label": text.getText("label-copy", ""),
			"tooltip": text.getText("tooltip-copy", "")
		}] />
	</#if>
	<#if action.hasPermission("filepool_update")>
		<#assign _actions_ = _actions_ + [{
			"action": "filepool_update",
			"icon": text.getText("icon-update"),
			"label": text.getText("label-update", ""),
			"tooltip": text.getText("tooltip-update", "")
		}] />
	</#if>
	<#if action.hasPermission("filepool_delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "filepool_delete",
			"icon": text.getText("icon-delete"),
			"label": text.getText("label-delete", ""),
			"tooltip": text.getText("tooltip-delete", "")
		}] />
	</#if>
	<#if _actions_?has_content || _ash_?has_content>
		<#if !(_ash_?has_content)>
			<#assign _ash_ = text.getText("_actions_", "")/>
		</#if>
		<#assign _actionc_ = [{
			"name": "_actions_",
			"type": "actions",
			"header": _ash_,
			"actions": _actions_,
			"nowrap": true,
			"fixed": true
		}] />
	</#if>
	<#if text.getText("listview-actions-align", "left") == "left">
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<#if action.hasPermission("filepool_bdelete")>
		<#assign _columns_ = _columns_ + [{
			"name": "_check_",
			"type": "check",
			"nowrap": true,
			"fixed": true
		}] />
	</#if>

	<#assign _columns_ = _columns_ + [{
			"name" : "id",
			"pkey" : true,
			"header": text.getText("d.id"),
			"filter": {
				"type": "number"
			},
			"link": true,
			"sortable": true,
			"tooltip": text.getText("d.id-tip", "")
		}, {
			"name" : "name",
			"header": text.getText("d.name"),
			"filter": {
				"type": "string"
			},
			"sortable": true,
			"tooltip": text.getText("d.name-tip", "")
		}, {
			"name" : "size",
			"header": text.getText("d.size"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": text.getText("d.size-tip", "")
		}, {
			"name" : "date",
			"header": text.getText("d.date"),
			"format": {
				"type": "timestamp"
			},
			"filter": {
				"type": "datetime"
			},
			"sortable": true,
			"tooltip": text.getText("d.date-tip", "")
		}, {
			"name" : "flag",
			"header": text.getText("d.flag"),
			"format": {
				"type": "integer"
			},
			"filter": {
				"type": "number"
			},
			"sortable": true,
			"tooltip": text.getText("d.flag-tip", "")
		}
	] />

	<#if text.getText("listview-actions-align", "") == "right">
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<@p.listview id="filepool_list" action="filepool_list" 
		list=result columns=_columns_ pager="true"
		link={ "action": "filepool_view", "params": { "d.id": "id" } }
	>
		<@s.param name="tools">
			<#if action.hasPermission("filepool_bdelete")>
				<@p.submit icon="icon-bdelete" action="filepool_bdelete" onclick="return filepool_list_bdelete();" theme="simple"><@p.text name="button-bdelete"/></@p.submit>
			</#if>
			<#if action.hasPermission("filepool_list_print")>
				<@p.submit icon="icon-print" onclick="return filepool_list_print();" theme="simple"><@p.text name="button-print"/></@p.submit>
			</#if>
			<#if action.hasPermission("filepool_list_csv")>
				<@p.submit icon="icon-csv" onclick="return filepool_list_csv();" theme="simple"><@p.text name="button-csv"/></@p.submit>
			</#if>
		</@s.param>
	</@p.listview>

	<script type="text/javascript"><!--
		function filepool_list_bdelete() {
			return nlv_submitCheckedKeys('filepool_list', '<@p.url action="filepool_bdelete"/>');
		}
		function filepool_list_print() {
			window.open("<@p.url action='filepool_list_print' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function filepool_list_csv() {
			window.open("<@p.url action='filepool_list_csv' includeParams='all' escapeAmp='false'/>");
			return false;
		}
	--></script>
</div>

</body>
</html>
