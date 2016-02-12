<html>
<head>
	<title><@p.text name="title-edit"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-edit"/></li>
			<li class="active"><@p.text name="step-edit-confirm"/></li>
		</ol>
	</div>
<#if text.getText("well-edit", "")?has_content>
	<div class="p-well"><@p.text name="well-edit"/></div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if action.hasPermission("~/list")><li><@p.a icon="icon-list" action="~/list" label="#(button-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-cform" id="resource" initfocus="true" method="post" theme="bs3h">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("clazz")>
			<@p.viewfield
				key="clazz"
				value="%{r.clazz}"
			/>
	</#if>
	<#if a.displayField("language")>
			<@p.viewfield
				key="language"
				value="%{r.language}"
				list="%{consts.localeLanguageMap}"
			/>
	</#if>
	<#if a.displayField("country")>
			<@p.viewfield
				key="country"
				value="%{r.country}"
				list="%{consts.localeCountryMap}"
			/>
	</#if>
	<#if a.displayField("source")>
			<@p.viewfield
				key="source"
				value="%{r.source}"
				escape="phtml"
			/>
	</#if>
	<#if a.displayField("status")>
			<@p.viewfield
				key="status"
				value="%{r.status}"
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
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-edit-execute" action="~/edit_execute"><@p.text name="button-edit-execute"/></@p.submit>
			<@p.submit icon="icon-back" action="~/edit_input"><@p.text name="button-back"/></@p.submit>
		</@p.div>
	</@p.form>
<#else>
	<div class="p-tcenter">
		<@p.a btn="default" icon="back" href="#" onclick="window.history.back();return false;" label="#(button-back)"/>
	</div>
</#if>
</div>

</body>
</html>
