<html>
<head>
	<title><@p.text name="title-copy"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<ol class="breadcrumb">
			<li><@p.i icon="icon"/> <@p.text name="title"/></li>
			<li class="active"><@p.text name="step-copy"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-copy", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<#include "/action-alert.ftl"/>

	<@p.form cssClass="p-eform" id="pages" method="post">
	<#if a.displayField("id")>
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("slug")>
			<@p.textfield
				key="slug"
				value="%{r.slug}"
				maxlength="100"
				size=""
			/>
	</#if>
	<#if a.displayField("title")>
			<@p.textfield
				key="title"
				value="%{r.title}"
				required="true"
				maxlength="100"
				size=""
			/>
	</#if>
	<#if a.displayField("tag")>
			<@p.textfield
				key="tag"
				value="%{r.tag}"
				maxlength="255"
				size=""
			/>
	</#if>
	<#if a.displayField("publishDate")>
			<@p.datetimepicker
				key="publishDate"
				value="%{r.publishDate}"
				maxlength="20"
				size="22"
			/>
	</#if>
	<#if a.displayField("thumbnail")>
			<@p.textfield
				key="thumbnail"
				value="%{r.thumbnail}"
				maxlength="32"
				size=""
			/>
	</#if>
	<#if a.displayField("content")>
			<@p.htmleditor
				key="content"
				value="%{r.content}"
				maxlength="100000"
				cols=""
				mediaAction="%{(!!(b.media_path)|||'/media') + '/browse.popup'}"
				height="400"
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
	<#if a.inputConfirm>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-copy-confirm",
			"action": "./copy.confirm",
			"text": "btn-copy-confirm"
		}]/>
	<#else>
		<#assign _buttons_ = _buttons_ + [{
			"icon": "icon-copy-execute",
			"action": "./copy.execute",
			"text": "btn-copy-execute"
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

		<script type="text/javascript"><!--
		
			function onPageLoad() {
			}
		--></script>
</div>

	<@safeinclude path="PageEditAction-footer.ftl"/>
</body>
</html>
