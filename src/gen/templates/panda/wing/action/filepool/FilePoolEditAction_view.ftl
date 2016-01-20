<html>
<head>
	<title><@p.text name="title-view"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-view"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<div class="p-toolbar-wrap"><ul class="p-toolbar">
		<li><@p.a icon="icon-refresh" href="javascript:location.reload(true)" label="#(button-refresh)"/>
</li><#if r?? && action.hasDataPermission(r, "~/print")><li><@p.a icon="icon-print" target="_blank" action="~/print"><@p.param name="id" value="%{r.id}"/><@p.text name='button-print'/></@p.a>
</li></#if><#if action.hasPermission("~/list")><li><@p.a icon="icon-list" action="~/list" label="#(button-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-vform" id="filepool" initfocus="true" method="post" theme="bs3h">
			<@n.viewfield
				key="id"
				value="%{r.id}"
			/>
			<@p.viewfield
				key="name"
				value="%{r.name}"
			/>
			<@p.viewfield
				key="size"
				value="%{r.size}"
			/>
			<@p.viewfield
				key="date"
				value="%{r.date}"
				format="timestamp"
			/>
			<@p.viewfield
				key="flag"
				value="%{r.flag}"
				format="check"
			/>
			<@p.viewfield
				key="readable"
				value="%{r.readable}"
				format="check"
			/>
			<@p.viewfield
				key="writeable"
				value="%{r.writeable}"
				format="check"
			/>
			<#assign _buttons_ = [] />
			<#if action.hasDataPermission(params!, '~/update')>
				<@p.url var="_u_" action="~/update" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-update",
					"onclick": "location.href='${vars._u_?js_string}'; return false;",
					"text": "button-update"
				}]/>
			</#if>
			<#if action.hasDataPermission(params!, '~/copy')>
				<@p.url var="_u_" action="~/copy" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-copy",
					"onclick": "location.href='${vars._u_?js_string}'; return false;",
					"text": "button-copy"
				}]/>
			</#if>
			<#if action.hasPermission('~/insert')>
				<@p.url var="_u_" action="~/insert"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-insert",
					"onclick": "location.href='${vars._u_?js_string}'; return false;",
					"text": "button-insert"
				}]/>
			</#if>
			<#if action.hasDataPermission(params!, '~/delete')>
				<@p.url var="_u_" action="~/delete" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-delete",
					"onclick": "location.href='${vars._u_?js_string}'; return false;",
					"text": "button-delete"
				}]/>
			</#if>
			<#if action.hasPermission('~/list')>
				<@p.url var="_u_" action="~/list"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-list",
					"onclick": "location.href='${vars._u_?js_string}'; return false;",
					"text": "button-list"
				}]/>
			</#if>
			<#include "/panda/mvc/view/form-buttons.ftl"/>
			<@form_buttons buttons=_buttons_/>
	</@p.form>
<#else>
	<div class="p-tcenter">
		<@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
</#if>
</div>

</body>
</html>
