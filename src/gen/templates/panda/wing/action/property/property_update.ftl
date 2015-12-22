<html>
<head>
	<title><@p.text name="title-update"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<ul class="p-toolbar">
	<#if action.hasPermission("property_list")>
		<li><@p.a icon="icon-list" action="property_list"><@p.text name='button-list'/></@p.a></li>
	</#if>
	</ul>


	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form cssClass="n-eform" id="property" initfocus="true" method="post" theme="bs3h">
			<@p.viewfield
				key="d.id"
				required="true"
			/>
			<@p.textfield
				key="d.clazz"
				required="true"
				maxlength="100"
				size="60"
			/>
			<@p.select
				key="d.language"
				required="true"
				emptyOption="false"
				list="consts.localeLanguageMap"
			/>
			<@p.select
				key="d.country"
				required="true"
				emptyOption="false"
				list="consts.localeCountryMap"
			/>
			<@p.textfield
				key="d.name"
				required="true"
				maxlength="50"
				size="60"
			/>
			<@p.textarea
				key="d.value"
				maxlength="5000"
				cols="60"
				rows="8"
			/>
			<@p.textarea
				key="d.memo"
				maxlength="1000"
				cols="60"
				rows="5"
			/>
			<@p.radio
				key="d.status"
				emptyOption="true"
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
	<#if action.getTextAsBoolean('ui-input-confirm', false)>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-update-confirm",
			"action": "property_update_confirm",
			"text": "button-update-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-update-execute",
			"action": "property_update_execute",
			"text": "button-update-execute"
		}]/>
	</#if>
			<#if action.hasPermission('property_list')>
				<@p.url var="_u_" action="property_list"/>
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