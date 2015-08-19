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
	<#if action.hasDataPermission(d!, "filepool_print")>
		<li><@p.a icon="icon-print" target="_blank" action="filepool_print"><@s.param name="d.id" value="d.id"/><@p.text name='button-print'/></@p.a></li>
	</#if>
	<#if action.hasPermission("filepool_list")>
		<li><@p.a icon="icon-list" action="filepool_list"><@p.text name='button-list'/></@p.a></li>
	</#if>
	</ul>


	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form cssClass="p-sform" id="filepool" initfocus="true" method="post" theme="bs3h">
			<@n.viewfield
				key="d.id"
			/>
			<@p.viewfield
				key="d.name"
			/>
			<@p.viewfield
				key="d.size"
			/>
			<@p.viewfield
				key="d.date"
				format="timestamp"
			/>
			<@p.viewfield
				key="d.flag"
				format="check"
			/>
			<@p.viewfield
				key="d.readable"
				format="check"
			/>
			<@p.viewfield
				key="d.writeable"
				format="check"
			/>
			<#assign _buttons_ = [] />
			<#if action.hasDataPermission(d!, 'filepool_update')>
				<@p.url var="_u_" action="filepool_update" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-update",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-update"
				}]/>
			</#if>
			<#if action.hasDataPermission(d!, 'filepool_copy')>
				<@p.url var="_u_" action="filepool_copy" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-copy",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-copy"
				}]/>
			</#if>
			<#if action.hasPermission('filepool_insert')>
				<@p.url var="_u_" action="filepool_insert"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-insert",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-insert"
				}]/>
			</#if>
			<#if action.hasDataPermission(d!, 'filepool_delete')>
				<@p.url var="_u_" action="filepool_delete" escapeAmp="false">
					<@s.param name="d.id" value="d.id"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-delete",
					"onclick": "location.href='${_u_?js_string}'; return false;",
					"text": "button-delete"
				}]/>
			</#if>
			<#if action.hasPermission('filepool_list')>
				<@p.url var="_u_" action="filepool_list"/>
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
