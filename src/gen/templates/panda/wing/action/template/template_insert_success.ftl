<html>
<head>
	<title><@p.text name="title-insert"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<ul class="p-toolbar">
	<#if action.hasDataPermission(d!, "template_print")>
		<li><@p.a icon="icon-print" target="_blank" action="template_print"><@s.param name="d.id" value="d.id"/><@p.text name='button-print'/></@p.a></li>
	</#if>
		<li><@p.a icon="icon-pdf-print" target="_blank" action="pdf" namespace="/">			<@s.param name="url"><@p.url action='template_print' forceAddSchemeHostAndPort='true' escapeAmp='false'>
					<@s.param name="d.id" value="d.id"/>
			</@p.url></@s.param><@p.text name='button-pdf-print'/></@p.a></li>
	<#if action.hasPermission("template_list")>
		<li><@p.a icon="icon-list" action="template_list"><@p.text name='button-list'/></@p.a></li>
	</#if>
	</ul>


	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form cssClass="p-sform" id="template" initfocus="true" method="post" theme="bs3h">
			<@p.viewfield
				key="d.id"
			/>
			<@p.viewfield
				key="d.name"
			/>
			<@p.viewfield
				key="d.language"
				list="consts.localeLanguageMap"
			/>
			<@p.viewfield
				key="d.country"
				list="consts.localeCountryMap"
			/>
			<@p.viewfield
				key="d.source"
			/>
			<@p.viewfield
				key="d.status"
				list="consts.dataStatusMap"
			/>
			<@p.viewfield
				key="d.uusid"
			/>
			<@p.viewfield
				key="d.uusnm"
			/>
			<@p.viewfield
				key="d.utime"
				format="datetime"
			/>
			<#assign _buttons_ = [] />
			<#if action.hasDataPermission(d!, 'template_update')>
				<@p.url var="_u_" action="template_update" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-update",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-update"
				}]/>
			</#if>
			<#if action.hasDataPermission(d!, 'template_copy')>
				<@p.url var="_u_" action="template_copy" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-copy",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-copy"
				}]/>
			</#if>
			<#if action.hasPermission('template_insert')>
				<@p.url var="_u_" action="template_insert"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-insert",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-insert"
				}]/>
			</#if>
			<#if action.hasDataPermission(d!, 'template_delete')>
				<@p.url var="_u_" action="template_delete" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-delete",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-delete"
				}]/>
			</#if>
			<#if action.hasPermission('template_list')>
				<@p.url var="_u_" action="template_list"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-list",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-list"
				}]/>
			</#if>
			<#include "/panda/exts/struts2/views/form-buttons.ftl"/>
			<@form_buttons buttons=_buttons_/>
	</@p.form>
</div>

</body>
</html>
