<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<style>
.pages-list {
	margin: 15px 0;
}
.page-block {
	display: inline-block;
	margin: 3px;
	min-width: 200px;
	text-align: center;
}
.page-title {
	padding: 5px;
}
</style>

<div class="p-section" id="a_pages_browser">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>

	<form id="pages_form" action="search" method="GET" loadmask="false" role="search">
		<div class="input-group">
			<@p.textfield cssClass="form-control" name="key" maxlength="80" placeholder="#(placeholder-search)"/>
			<span class="input-group-btn">
				<@p.submit icon="search"/>
			</span>
		</div>
	</form>
	<br/>

	<#include "/action-alert.ftl"/>


<#if result?has_content>
	<#if methodName == "searchs">
		<#include "/panda/app/view/index-link.ftl"/>
	<#else>
		<#assign _index_pager_link_ = "p!{page}"/>
	</#if>

<#if result?size gt 30>
	<#include "/panda/app/view/index-topbar.ftl"/>
</#if>

	<@p.url var="mp" action="%{!!(b.media_path)|||'/media'}"/>

	<div class="pages-list">
	<#list result as t>
		<a class="page-block" href="v/<#if t.slug?has_content>${t.slug}<#else>${t.id}</#if>">
			<img class="img-thumbnail" src="${v.mp}/thumb/${t.thumbnail!}">
			<div class="page-title">${t.title?html}</div>
		</a>
	</#list>
	</div>

	<#include "/panda/app/view/index-bombar.ftl"/>
</#if>
</div>

</body>
</html>
