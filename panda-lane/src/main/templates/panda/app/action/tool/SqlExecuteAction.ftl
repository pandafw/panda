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

	<@p.form id="sqlexec" method="post" theme="simple" focusme="#sqlexec_sql">
	<table class="sqlexec-tb" style="width: 100%">
		<tr><td>SQL: (Ctrl+Enter to execute)</td>
			<td align="right">
				<@p.checkbox name="autoCommit" fieldLabel="Auto Commit" fieldValue="true"/>
				<@p.checkbox name="ignoreError" fieldLabel="Ignore Error" fieldValue="true"/>
			</td>
		</tr>
		<tr><td colspan="2"><@p.textarea name="sql" rows="8" cssStyle="width:100%" enterfire="#sqlexec_submit"/></td></tr>
		<tr><td colspan="2" align="center"><@p.submit id="sqlexec_submit" icon="flash" label="#(btn-exec)"/></td></tr>
	</table>
	</@p.form>
	
	<hr/>

<#if result??>
<#list result as r>
	<div class="panel panel-success">
		<div class="panel-heading">${(r_index + 1)?c}. <@p.property value=r.sql format="phtml"/></div>
		<div class="panel-body">
			<#if r.updateCount &gt; 0>${r.updateCount} rows updated.<br/></#if>
			<#if r.resultSet?has_content>
			<div class="table-responsive">
			<table class="table table-striped table-bordered p-fz80p p-th-nowrap p-td-nowrap">
				<#list r.resultSet as row>
					<#if row_index == 0>
						<thead><tr><#list row as _h><th>${assist.escapePhtml(_h!"")}</th></#list></tr></thead>
					<#else>
						<#if row_index == 1><tbody></#if>
						<tr><#list row as _v><td>${assist.escapePhtml(_v!"")}</td></#list></tr>
						<#if !row_has_next></tbody></#if>
					</#if>
				</#list>
			</table>
			</div>
			</#if>
			<#if r.error?has_content><pre class="p-error">${r.error?html}</pre></#if>
		</div>
	</div>
	<br/>
</#list>
</#if>

</div>

</body>
</html>
