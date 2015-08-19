<html>
<head>
	<title><@p.text name="title-copy"><@p.param name="title" value="#(title)"/></@p.text></title>
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

	<@p.form cssClass="p-eform" id="filepool" initfocus="true" method="post" theme="bs3h">
			<@n.viewfield
				key="d.id"
			/>
			<@p.textfield
				key="d.name"
				required="true"
				maxlength="255"
				size="50"
			/>
			<@p.viewfield
				key="d.size"
			/>
			<@p.datetimepicker
				key="d.date"
				format="timestamp"
				maxlength="28"
				size="30"
			/>
			<@p.checkbox
				key="d.flag"
			/>
			<@p.checkbox
				key="d.readable"
			/>
			<@p.checkbox
				key="d.writeable"
			/>
		<#assign _buttons_ = [] />
	<#if action.getTextAsBoolean('ui-input-confirm', false)>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-copy-confirm",
			"action": "filepool_copy_confirm",
			"text": "button-copy-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-copy-execute",
			"action": "filepool_copy_execute",
			"text": "button-copy-execute"
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

		<script type="text/javascript"><!--
				--></script>
</div>

</body>
</html>
