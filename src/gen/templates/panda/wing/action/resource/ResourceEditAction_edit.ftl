<html>
<head>
	<title><@p.text name="title-edit"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-edit"/></li>
		</ol>
	</div>
<#if text.getText("well-edit", "")?has_content>
	<div class="p-well"><@p.text name="well-edit"/></div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.hasPermission("~/list")><li><@p.a icon="icon-list" action="~/list" label="#(button-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="n-eform" id="resource" initfocus="true" method="post" theme="bs3h">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
				required="true"
			/>
	</#if>
	<#if a.displayField("clazz")>
			<@p.textfield
				key="clazz"
				value="%{r.clazz}"
				required="true"
				maxlength="100"
				size="60"
			/>
	</#if>
	<#if a.displayField("language")>
			<@p.select
				key="language"
				value="%{r.language}"
				required="true"
				emptyOption="false"
				list="%{consts.localeLanguageMap}"
			/>
	</#if>
	<#if a.displayField("country")>
			<@p.select
				key="country"
				value="%{r.country}"
				required="true"
				emptyOption="false"
				list="%{consts.localeCountryMap}"
			/>
	</#if>
	<#if a.displayField("source")>
			<@p.textarea
				key="source"
				value="%{r.source}"
				maxlength="50000"
				cols="60"
				rows="20"
			/>
	</#if>
	<#if a.displayField("status")>
			<@p.radio
				key="status"
				value="%{r.status}"
				emptyOption="true"
				list="%{consts.dataStatusMap}"
			/>
	</#if>
	<#if a.displayField("uusid")>
			<@p.viewfield
				key="uusid"
				value="%{r.uusid}"
			/>
	</#if>
	<#if a.displayField("uusnm")>
			<@p.viewfield
				key="uusnm"
				value="%{r.uusnm}"
			/>
	</#if>
	<#if a.displayField("utime")>
			<@p.viewfield
				key="utime"
				value="%{r.utime}"
				format="datetime"
			/>
	</#if>
		<#assign _buttons_ = [] />
	<#if a.inputConfirm>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-edit-confirm",
			"action": "~/edit_confirm",
			"text": "button-edit-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-edit-execute",
			"action": "~/edit_execute",
			"text": "button-edit-execute"
		}]/>
	</#if>
			<#include "/panda/mvc/view/form-buttons.ftl"/>
			<@form_buttons buttons=_buttons_/>
	</@p.form>

		<script type="text/javascript"><!--
		
			function onPageLoad() {
			}
		--></script>
<#else>
	<div class="p-tcenter">
		<@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
</#if>
</div>

</body>
</html>
