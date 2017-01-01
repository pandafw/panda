<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_sqlexec">
	<div class="p-header">
		<h3><i class="fa fa-strikethrough"></i> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<!-- Nav tabs -->
	<ul class="nav nav-tabs">
		<li<#if !(params.f??)> class="active"</#if>><a href="#hash_string" data-toggle="tab">String hash</a></li>
		<li<#if params.f??> class="active"</#if>><a href="#hash_file" data-toggle="tab">File hash</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
		<div class="tab-pane<#if !(params.f??)> active</#if>" id="hash_string">
			<@p.form id="hashs" action="~/" method="get" theme="bs3h" showDescrip="true">
				<@p.textarea key="s" rows="3" enterfire="form"/>
				<@p.div cssClass="p-buttons">
					<@p.submit icon="fa fa-gear" label="#(button-hash)"/>
				</@p.div>
			</@p.form>
		</div>
		<div class="tab-pane<#if params.f??> active</#if>" id="hash_file">
			<@p.form id="hashf" action="~/" method="post" enctype="multipart/form-data" theme="bs3h" showDescrip="true">
				<@p.file
					key="f"
					value=""
					size="50"
				/>
				<@p.div cssClass="p-buttons">
					<@p.submit icon="fa fa-gear" label="#(button-hash)"/>
				</@p.div>
			</@p.form>
		</div>
	</div>


<#if result??>
	<hr/>
	<div class="panel panel-success">
		<div class="panel-heading"><#if (params.s)?has_content>String: . <@p.property value="p.s" format="phtml"/><#else>File: ${params.f.name?html}</#if></div>
		<div class="panel-body">
			<div class="table-responsive">
			<table class="table table-striped table-bordered p-fz80p p-th-nowrap p-td-nowrap">
			<tbody>
			<#list result?keys as k>
				<tr><td>${k?html}</td><td>${result[k]?html}</td></tr>
			</#list>
			</tbody>
			</table>
			</div>
		</div>
	</div>
	<br/>
</#if>
</div>

</body>
</html>
