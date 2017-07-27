<html>
<head>
	<title><@p.text name="title-delete"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-delete"/></li>
			<li class="active"><@p.text name="step-delete-confirm"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-delete-confirm", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-cform" id="filepool" focusme="true" method="post" theme="bs3h">
	<#if a.displayField("id")>
			<@n.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("name")>
			<@p.viewfield
				key="name"
				value="%{r.name}"
			/>
	</#if>
	<#if a.displayField("size")>
			<@p.viewfield
				key="size"
				value="%{r.size}"
			/>
	</#if>
	<#if a.displayField("date")>
			<@p.viewfield
				key="date"
				value="%{r.date}"
				format="timestamp"
			/>
	</#if>
	<#if a.displayField("flag")>
			<@p.viewfield
				key="flag"
				value="%{r.flag}"
			/>
	</#if>
		<#assign _buttons_ = [{
			"icon": "icon-delete-execute",
			"action": "~/delete_execute",
			"text": "btn-delete-execute"
		}]/>
			<#if a.canAccess('~/list')>
				<@p.url var="_u_" action="~/list"/>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-list",
					"text": "btn-list",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#include "/panda/mvc/view/form-buttons.ftl"/>
			<@form_buttons buttons=_buttons_/>
	</@p.form>
<#else>
	<div class="p-tcenter">
		<@p.a href="#" onclick="window.history.back();return false;" btn="default" icon="back" label="#(btn-back)"/>
	</div>
</#if>
</div>

</body>
</html>
