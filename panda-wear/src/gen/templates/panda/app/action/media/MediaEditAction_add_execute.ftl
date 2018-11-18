<html>
<head>
	<title><@p.text name="title-add"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-add"/></li>
			<li class="active"><@p.text name="step-add-success"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-add-success", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if r?? && a.canAccessData("~/print", r)><li><@p.a action="~/print" icon="icon-print" label="#(btn-print)" target="_blank"><@p.param name="id" value="%{r.id}"/></@p.a>
</li></#if><#if a.canAccess("~/add")><li><@p.a action="~/add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if a.canAccess("~/list")><li><@p.a action="~/list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-sform" id="media" focusme="true" method="post" theme="bs3h">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("mediaName")>
			<@p.viewfield
				key="mediaName"
				value="%{r.mediaName}"
			/>
	</#if>
	<#if a.displayField("mediaSize")>
			<@p.viewfield
				key="mediaSize"
				value="%{r.mediaSize}"
				format="filesize"
			/>
	</#if>
	<#if a.displayField("mediaWidth")>
			<@p.viewfield
				key="mediaWidth"
				value="%{r.mediaWidth}"
			/>
	</#if>
	<#if a.displayField("mediaHeight")>
			<@p.viewfield
				key="mediaHeight"
				value="%{r.mediaHeight}"
			/>
	</#if>
	<#if a.displayField("mediaFile")>
			<@p.uploader
				key="mediaFile"
				value="%{r.mediaFile}"
				readonly="true"
				accept="image/*"
				size="30"
				uploadAction="%{b.files_path + '/upload'}"
				uploadName="file"
				dnloadAction="%{b.files_path + '/download'}"
				dnloadName="id"
				defaultAction="mediaview"
				defaultParams="!{'id': '%{r.id}'}"
				defaultEnable="%{r.id != null && r.mediaSize > 0}"
			/>
	</#if>
	<#if a.displayField("updatedAt")>
			<@p.viewfield
				key="updatedAt"
				value="%{r.updatedAt}"
				format="datetime"
			/>
	</#if>
	<#if a.displayField("updatedBy")>
			<@p.hidden
				name="updatedBy"
				value="%{r.updatedBy}"
			/>
	</#if>
	<#if a.displayField("updatedByUser")>
			<@p.viewfield
				key="updatedByUser"
				value="%{r.updatedByUser}"
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
</#if>
</div>

</body>
</html>
