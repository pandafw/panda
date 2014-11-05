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
