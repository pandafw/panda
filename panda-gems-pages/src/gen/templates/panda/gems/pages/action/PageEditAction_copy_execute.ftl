<html>
<head>
	<title><@p.text name="title-copy"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li><@p.text name="step-copy"/></li>
			<li class="active"><@p.text name="step-copy-success"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-copy-success", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./add")><li><@p.a action="./add" icon="icon-new" label="#(btn-new)"/>
</li></#if><#if r?? && a.canAccessData("./print", r)><li><@p.a action="./print" icon="icon-print" label="#(btn-print)" target="_blank"><@p.param name="id" value="%{r.id}"/></@p.a>
</li></#if><#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-sform" id="pages" method="post">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("slug")>
			<@p.viewfield
				key="slug"
				value="%{r.slug}"
			/>
	</#if>
	<#if a.displayField("title")>
			<@p.viewfield
				key="title"
				value="%{r.title}"
			/>
	</#if>
	<#if a.displayField("tag")>
			<@p.viewfield
				key="tag"
				value="%{r.tag}"
			/>
	</#if>
	<#if a.displayField("publishDate")>
			<@p.viewfield
				key="publishDate"
				value="%{r.publishDate}"
				format="datetime"
			/>
	</#if>
	<#if a.displayField("thumbnail")>
			<@p.viewfield
				key="thumbnail"
				value="%{r.thumbnail}"
			/>
	</#if>
	<#if a.displayField("content")>
			<@p.viewfield
				key="content"
				value="%{r.content}"
				format="html"
			/>
	</#if>
	<#if a.displayField("status")>
			<@p.viewfield
				key="status"
				value="%{r.status}"
				list="%{consts.dataStatusMap}"
			/>
	</#if>
	<#if a.displayField("createdAt")>
			<@p.viewfield
				key="createdAt"
				value="%{r.createdAt}"
				format="datetime"
			/>
	</#if>
	<#if a.displayField("createdBy")>
			<@p.hidden
				name="createdBy"
				value="%{r.createdBy}"
			/>
	</#if>
	<#if a.displayField("createdByName")>
			<@p.hidden
				name="createdByName"
				value="%{r.createdByName}"
			/>
	</#if>
	<#if a.displayField("createdByUser")>
			<@p.viewfield
				key="createdByUser"
				value="%{r.createdByUser}"
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
	<#if a.displayField("updatedByName")>
			<@p.hidden
				name="updatedByName"
				value="%{r.updatedByName}"
			/>
	</#if>
	<#if a.displayField("updatedByUser")>
			<@p.viewfield
				key="updatedByUser"
				value="%{r.updatedByUser}"
			/>
	</#if>
			<#assign _buttons_ = [] />
			<#if r?? && a.canAccessData('./edit', r)>
				<@p.url var="_u_" action="./edit" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-edit",
					"text": "btn-edit",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#if r?? && a.canAccessData('./copy', r)>
				<@p.url var="_u_" action="./copy" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-copy",
					"text": "btn-copy",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#if r?? && a.canAccessData('./delete', r)>
				<@p.url var="_u_" action="./delete" escapeAmp="false">
					<@p.param name="id" value="%{r.id}"/>
				</@p.url>
				<#assign _buttons_ = _buttons_ + [{
					"icon": "icon-delete",
					"text": "btn-delete",
					"onclick": "location.href='${vars._u_?js_string}'; return false;"
				}]/>
			</#if>
			<#if a.canAccess('./list')>
				<@p.url var="_u_" action="./list"/>
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

	<@safeinclude path="PageEditAction-footer.ftl"/>
</body>
</html>
