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
			<li class="active"><@p.text name="step-add-confirm"/></li>
		</ol>
	</div>
<#assign _well = a.getText("well-add-confirm", "")/>
<#if _well?has_content>
	<div class="p-well">${_well}</div>
</#if>


	<div class="p-toolbar-wrap"><ul class="p-toolbar">
<#if a.canAccess("./list")><li><@p.a action="./list" icon="icon-list" label="#(btn-list)"/>
</li></#if>	</ul><div class="clearfix"></div></div>

	<div id="media_alert"><#include "/action-alert.ftl"/></div>

<#if r??>
	<@p.form cssClass="p-cform" id="media" method="post">
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
	<#if a.displayField("tag")>
			<@p.viewfield
				key="tag"
				value="%{r.tag}"
				list="%{consts.mediaTagMap}"
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
				format="size"
			/>
	</#if>
	<#if a.displayField("width")>
			<@p.viewfield
				key="width"
				value="%{r.width}"
			/>
	</#if>
	<#if a.displayField("height")>
			<@p.viewfield
				key="height"
				value="%{r.height}"
			/>
	</#if>
	<#if a.displayField("file")>
			<@p.uploader
				key="file"
				value="%{r.file}"
				readonly="true"
				accept="image/*"
				size="30"
				uploadUrl="%{b.files_path + '/upload'}"
				dnloadUrl="%{b.files_path + '/download?file=$'}"
				defaultAction="media"
				defaultParams="!{'slug': '%{r.slug}'}"
				defaultEnable="%{r.id != null && r.size > 0}"
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
		<@p.div cssClass="p-buttons">
			<@p.submit action="./add.execute" icon="icon-add-execute" label="#(btn-add-execute)"/>
			<@p.submit action="./add.input" icon="icon-back" label="#(btn-back)"/>
		</@p.div>
	</@p.form>
<#else>
	<div class="p-tcenter">
		<@p.a href="#" onclick="window.history.back();return false;" btn="default" icon="back" label="#(btn-back)"/>
	</div>
</#if>
</div>

</body>
</html>
