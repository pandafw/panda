<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_sqlexec">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<!-- Nav tabs -->
	<ul class="nav nav-tabs">
		<li<#if !(params.f??)> class="active"</#if>><a href="#hash_string" data-toggle="tab"><@p.text name="p.s"/></a></li>
		<li<#if params.f??> class="active"</#if>><a href="#hash_file" data-toggle="tab"><@p.text name="p.f"/></a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
		<div class="tab-pane<#if !(params.f??)> active</#if>" id="hash_string">
			<@p.form id="hashs" method="post" theme="bs3h">
				<@p.textarea key="s" rows="3" enterfire="form"/>
				<@p.div cssClass="p-buttons">
					<@p.submit icon="fa fa-hashtag" label="#(btn-hash)"/>
				</@p.div>
			</@p.form>
		</div>
		<div class="tab-pane<#if params.f??> active</#if>" id="hash_file">
			<@p.form id="hashf" method="post" enctype="multipart/form-data" theme="bs3h">
				<@p.file
					key="f"
					value=""
				/>
				<@p.div cssClass="p-buttons">
					<@p.submit icon="fa fa-hashtag" label="#(btn-hash)"/>
				</@p.div>
			</@p.form>
		</div>
	</div>

<#if result??>
	<hr/>
	<div class="panel panel-success">
		<div class="panel-heading p-breakword"><#if (params.s)?has_content>${assist.ellipsis(params.s, 30)?html}<#else>${params.f.name?html}</#if></div>
		<div class="panel-body">
			<table class="table table-striped table-bordered p-fz80p p-th-nowrap p-td-breakall">
			<tbody>
			<#list result?keys as k>
				<tr><th>${k?html}</th><td>${result[k]?html}</td></tr>
			</#list>
			</tbody>
			</table>
		</div>
	</div>
</#if>
</div>

</body>
</html>
