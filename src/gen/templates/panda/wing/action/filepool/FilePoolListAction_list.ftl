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

	<div class="p-toolbar-wrap"><ul class="p-toolbar">
		<li><@p.a icon="icon-refresh" href="javascript:location.reload(true)"><@p.text name='button-refresh'/></@p.a>
</li>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"header": text.getText("listview-th-number", ""),
		"nowrap": true,
		"fixed": true
	}] />

	<#assign _actions_ = [] />
	<#assign _ash_ = "" />
	<#if action.hasPermission("~/insert")>
		<@p.url var='_u_' action='~/insert'/>
		<#assign _ash_ = '<a class="n-lv-ia" href="' + vars._u_ + '" title="' + text.getText('tooltip-insert', '')?html + '"><i class="' + text.getText('icon-insert', '') + '"></i>' + text.getText('label-insert', '') + '</a>'/>
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
	<#if action.hasPermission("~/update")>
		<#assign _actions_ = _actions_ + [{
			"action": "~/update",
			"params": { "id": "%{top.id}" },
			"icon": text.getText("icon-update"),
			"label": text.getText("label-update", ""),
			"tooltip": text.getText("tooltip-update", "")
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
			"value": true,
			"header": text.getText("a.t.id"),
			"filter": {
				"type": "number"
			},
			"link": true,
			"sortable": true,
			"tooltip": text.getText("a.t.id-tip", "")
		}, {
			"name" : "name",
			"header": text.getText("a.t.name"),
			"filter": {
				"type": "string"
			},
			"sortable": true,
			"tooltip": text.getText("a.t.name-tip", "")
		}, {
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
		}, {
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
		}, {
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
		}
	] />

	<#if text.getText("listview-actions-align", "") == "right">
		<#assign _columns_ = _columns_ + _actionc_![]/>
	</#if>
	<@p.set var="lvtools">
		<#if action.hasPermission("~/bdelete")>
			<@p.submit icon="icon-bdelete" action="filepool_bdelete" onclick="return filepool_list_bdelete();" theme="simple"><@p.text name="button-bdelete"/></@p.submit>
		</#if>
		<#if action.hasPermission("~/list_print")>
			<@p.submit icon="icon-print" onclick="return filepool_list_print();" theme="simple"><@p.text name="button-print"/></@p.submit>
		</#if>
		<#if action.hasPermission("~/list_csv")>
			<@p.submit icon="icon-csv" onclick="return filepool_list_csv();" theme="simple"><@p.text name="button-csv"/></@p.submit>
		</#if>
		<#if action.hasPermission("~/list_json")>
			<@p.submit icon="icon-json" onclick="return filepool_list_json();" theme="simple"><@p.text name="button-json"/></@p.submit>
		</#if>
		<#if action.hasPermission("~/list_xml")>
			<@p.submit icon="icon-xml" onclick="return filepool_list_xml();" theme="simple"><@p.text name="button-xml"/></@p.submit>
		</#if>
		<#if action.hasPermission("/pdf")>
			<@p.submit icon="icon-pdf" onclick="return filepool_list_pdf();" theme="simple"><@p.text name="button-pdf"/></@p.submit>
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
			return plv_submitCheckedKeys('filepool_list', '<@p.url action="~/bdelete"/>');
		}
		function filepool_list_print() {
			window.open("<@p.url action='~/list_print' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function filepool_list_csv() {
			window.open("<@p.url action='~/list_csv' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function filepool_list_json() {
			window.open("<@p.url action='~/list_json' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function filepool_list_xml() {
			window.open("<@p.url action='~/list_xml' includeParams='all' escapeAmp='false'/>");
			return false;
		}
		function filepool_list_pdf() {
			window.open("<@p.url action='/pdf' escapeAmp='false'/>?url=" + encodeURIComponent("<@p.url action='~/list_print' forceAddSchemeHostAndPort='true' escapeAmp='false'/>" + location.search));
			return false;
		}
	--></script>
</div>

</body>
</html>
