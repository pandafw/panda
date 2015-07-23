<html>
<head>
	<title><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
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
	<#if action.hasPermission("property_insert")>
		<@p.url var='_u_' action='property_insert'/>
		<@p.text var='_icon_' name='icon-insert'/>
		<#assign _ash_ = '<a class="n-lv-ia" href="' + _u_ + '" title="' + text.getText("tooltip-insert", "")?html + '">'/>
		<#if _icon_?has_content>
			<#assign _ash_ = _ash_ + '<i class="fa ' + _icon_ + '"></i>'/>
		</#if>
		<#assign _ash_ = _ash_ + text.getText("label-insert", "") + '</a>'/>
	</#if>
	<#if action.hasPermission("property_copy")>
		<#assign _actions_ = _actions_ + [{
			"action": "property_copy",
			"icon": text.getText("icon-copy"),
			"label": text.getText("label-copy", ""),
			"tooltip": text.getText("tooltip-copy", "")
		}] />
	</#if>
	<#if action.hasPermission("property_update")>
		<#assign _actions_ = _actions_ + [{
			"action": "property_update",
			"icon": text.getText("icon-update"),
			"label": text.getText("label-update", ""),
			"tooltip": text.getText("tooltip-update", "")
		}] />
	</#if>
	<#if action.hasPermission("property_delete")>
		<#assign _actions_ = _actions_ + [{
			"action": "property_delete",
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

	<#if action.hasPermission("property_bdelete")>
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
			"hidden": false,
			"link": true,
			"sortable": true,
			"tooltip": text.getText("d.id-tip", "")
		}, {
			"name" : "clazz",
			"header": text.getText("d.clazz"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.clazz-tip", "")
		}, {
			"name" : "language",
			"header": text.getText("d.language"),
			"format": {
				"codemap": "consts.localeLanguageMap",
				"type": "code"
			},
			"filter": {
				"list": "consts.localeLanguageMap",
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.language-tip", "")
		}, {
			"name" : "country",
			"header": text.getText("d.country"),
			"format": {
				"codemap": "consts.localeCountryMap",
				"type": "code"
			},
			"filter": {
				"list": "consts.localeCountryMap",
				"type": "select"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.country-tip", "")
		}, {
			"name" : "name",
			"header": text.getText("d.name"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.name-tip", "")
		}, {
			"name" : "value",
			"header": text.getText("d.value"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.value-tip", "")
		}, {
			"name" : "memo",
			"header": text.getText("d.memo"),
			"filter": {
				"type": "string"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.memo-tip", "")
		}, {
			"name" : "status",
			"header": text.getText("d.status"),
			"format": {
				"codemap": "consts.dataStatusMap",
				"type": "code"
			},
			"filter": {
				"list": "consts.dataStatusMap",
				"type": "radio"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.status-tip", "")
		}, {
			"name" : "uusid",
			"header": text.getText("d.uusid"),
			"filter": {
				"type": "number"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.uusid-tip", "")
		}, {
			"name" : "utime",
			"header": text.getText("d.utime"),
			"format": {
				"type": "datetime"
			},
			"filter": {
				"type": "datetime"
			},
			"hidden": false,
			"link": false,
			"sortable": true,
			"tooltip": text.getText("d.utime-tip", "")
		}
	] />

	<#if text.getText("listview-actions-align", "") == "right">
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>

	<@p.listview id="property_list" action="property_list" 
		list=result columns=_columns_ cssColumn="status" pager="true"
		link={ "action": "property_view", "params": { "d.id": "id" } }
	>
		<@s.param name="tools">
			<#if action.hasPermission("property_bdelete")>
				<@p.submit icon="icon-bdelete" action="property_bdelete" onclick="return property_list_bdelete();" theme="simple"><@p.text name="button-bdelete"/></@p.submit>
			</#if>
			<#if action.hasPermission("property_list_print")>
				<@p.submit icon="icon-print" onclick="return property_list_print();" theme="simple"><@p.text name="button-print"/></@p.submit>
			</#if>
			<#if action.hasPermission("property_list_csv")>
				<@p.submit icon="icon-csv" onclick="return property_list_csv();" theme="simple"><@p.text name="button-csv"/></@p.submit>
			</#if>
			<#if action.hasPermission("property_list_print")>
				<@p.submit icon="icon-pdf-list_print" onclick="return property_list_pdf_list_print();" theme="simple"><@p.text name="button-pdf-list_print"/></@p.submit>
			</#if>
		</@s.param>
	</@p.listview>

	<script type="text/javascript"><!--
		function property_list_bdelete() {
			return nlv_submitCheckedKeys('property_list', '<@p.url action="property_bdelete"/>');
		}
		function property_list_print() {
			window.open("<@p.url action='property_list_print' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function property_list_csv() {
			window.open("<@p.url action='property_list_csv' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function property_list_pdf_list_print() {
			window.open("<@p.url action='pdf' namespace='/' escapeAmp='false'/>?url=" + encodeURIComponent("<@p.url action='property_list_print' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			return false;
		}
	--></script>
</div>

</body>
</html>
