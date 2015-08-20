<html>
<head>
	<title><@p.text name="title-list"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-list"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<ul class="p-toolbar">
		<li><@p.a icon="icon-refresh" href="javascript:location.reload(true)"><@p.text name='button-refresh'/></@p.a></li>
	</ul>

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
	<#if action.hasPermission("~/insert")>
		<@p.url var='_u_' action='~/insert'/>
		<@p.text var='_icon_' name='icon-insert'/>
		<#assign _ash_ = '<a class="n-lv-ia" href="' + vars._u_ + '" title="' + text.getText("tooltip-insert", "")?html + '">'/>
		<#if _icon_?has_content>
			<#assign _ash_ = _ash_ + '<i class="fa ' + _icon_ + '"></i>'/>
		</#if>
		<#assign _ash_ = _ash_ + text.getText("label-insert", "") + '</a>'/>
	</#if>
	<#if action.hasPermission("~/copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/copy",
			"icon": text.getText("icon-copy"),
			"label": text.getText("label-copy", ""),
			"tooltip": text.getText("tooltip-copy", "")
		}] />
	</#if>
	<#if action.hasPermission("~/update")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/update",
			"icon": text.getText("icon-update"),
			"label": text.getText("label-update", ""),
			"tooltip": text.getText("tooltip-update", "")
		}] />
	</#if>
	<#if action.hasPermission("~/delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/delete",
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

	<#if action.hasPermission("~/bdelete")>
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
			"header": text.getText("a.t.id"),
			"filter": {
				"type": "number"
			},
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": text.getText("a.t.id-tip", "")
		}, {
			"name" : "clazz",
			"header": text.getText("a.t.clazz"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": text.getText("a.t.clazz-tip", "")
		}, {
			"name" : "language",
			"header": text.getText("a.t.language"),
			"format": {
				"codemap": consts.localeLanguageMap,
				"type": "code"
			},
			"filter": {
				"list": consts.localeLanguageMap,
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.language-tip", "")
		}, {
			"name" : "country",
			"header": text.getText("a.t.country"),
			"format": {
				"codemap": consts.localeCountryMap,
				"type": "code"
			},
			"filter": {
				"list": consts.localeCountryMap,
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.country-tip", "")
		}, {
			"name" : "source",
			"header": text.getText("a.t.source"),
			"filter": {
				"type": "string"
			},
			"hidden": true,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.source-tip", "")
		}, {
			"name" : "status",
			"header": text.getText("a.t.status"),
			"format": {
				"codemap": consts.dataStatusMap,
				"type": "code"
			},
			"filter": {
				"list": consts.dataStatusMap,
				"type": "radio"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.status-tip", "")
		}, {
			"name" : "uusid",
			"header": text.getText("a.t.uusid"),
			"filter": {
				"type": "number"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.uusid-tip", "")
		}, {
			"name" : "utime",
			"header": text.getText("a.t.utime"),
			"format": {
				"type": "datetime"
			},
			"filter": {
				"type": "datetime"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("a.t.utime-tip", "")
		}
	] />

	<#if text.getText("listview-actions-align", "") == "right">
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<@p.listview id="resource_list" action="~/list" 
		list=result columns=_columns_ cssColumn="status"
		link={ "action": "~/view", "params": { "d.id": "id" } }
	>
		<@p.param name="tools">
			<#if action.hasPermission("~/bdelete")>
				<@p.submit icon="icon-bdelete" action="resource_bdelete" onclick="return resource_list_bdelete();" theme="simple"><@p.text name="button-bdelete"/></@p.submit>
			</#if>
			<#if action.hasPermission("~/list_print")>
				<@p.submit icon="icon-print" onclick="return resource_list_print();" theme="simple"><@p.text name="button-print"/></@p.submit>
			</#if>
			<#if action.hasPermission("~/list_csv")>
				<@p.submit icon="icon-csv" onclick="return resource_list_csv();" theme="simple"><@p.text name="button-csv"/></@p.submit>
			</#if>
			<#if action.hasPermission("~/list_print")>
				<@p.submit icon="icon-pdf" onclick="return resource_list_pdf();" theme="simple"><@p.text name="button-pdf"/></@p.submit>
			</#if>
		</@p.param>
	</@p.listview>

	<script type="text/javascript"><!--
		function resource_list_bdelete() {
			return nlv_submitCheckedKeys('resource_list', '<@p.url action="~/bdelete"/>');
		}
		function resource_list_print() {
			window.open("<@p.url action='~/list_print' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function resource_list_csv() {
			window.open("<@p.url action='~/list_csv' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function resource_list_pdf() {
			window.open("<@p.url action='pdf' escapeAmp='false'/>?url=" + encodeURIComponent("<@p.url action='~/list_print' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			return false;
		}
	--></script>
</div>

</body>
</html>
