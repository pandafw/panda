<html>
<head>
	<title><@p.text name="title-view"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-view"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-view", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
		<li><@p.a href="javascript:location.reload(true)" icon="icon-refresh" label="#(btn-refresh)"/>
</li><#if r?? && a.canAccessData("~/print", r)><li><@p.a action="~/print" icon="icon-print" label="#(btn-print)" target="_blank"><@p.param name="id" value="%{r.id}"/></@p.a>
</li></#if><#if a.canAccess("~/add")><li><@p.a action="~/add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if a.canAccess("~/list")><li><@p.a action="~/list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-vform" id="filepool" focusme="true" method="post" theme="bs3h">
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
			<#assign _buttons_ = [] />
			<#if r?? && a.canAccessData('~/edit', r)>
				<@p.url var="_u_" action="~/edit" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-edit",
					"text": "btn-edit",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#if r?? && a.canAccessData('~/copy', r)>
				<@p.url var="_u_" action="~/copy" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-copy",
					"text": "btn-copy",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#if r?? && a.canAccessData('~/delete', r)>
				<@p.url var="_u_" action="~/delete" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-delete",
					"text": "btn-delete",
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
