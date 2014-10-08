<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section" id="a_sqlexec">
	<div class="p-header">
		<h3><i class="fa fa-strikethrough"></i> <@p.text name="title"/></h3>
	</div>
	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form id="sqlexec" method="post" theme="simple">
	<table class="sqlexec-tb" style="width: 100%">
		<tr><td>SQL: (Ctrl+Enter to execute)</td>
			<td align="right">
				<@p.checkbox name="autoCommit" clabel="Auto Commit" fieldValue="true"/>
				<@p.checkbox name="ignoreError" clabel="Ignore Error" fieldValue="true"/>
			</td>
		</tr>
		<tr><td colspan="2"><@p.textarea name="sql" rows="15" cssStyle="width:100%"/></td></tr>
		<tr><td colspan="2" align="center"><@p.submit id="sqlexec_submit" icon="flash"><@p.text name="button-execute"/></@p.submit></td></tr>
	</table>
	</@p.form>
	
	<hr/>

<#if results??>
<#list results as r>
	<div class="panel panel-success">
		<div class="panel-heading">${(r_index + 1)?c}. <@p.property value=r.sql format="phtml"/></div>
		<div class="panel-body">
			<#if r.updateCount &gt; 0>${r.updateCount} rows updated.<br/></#if>
			<#if r.resultSet?has_content>
			<div class="table-responsive">
			<table class="table">
				<#list r.resultSet as row>
					<#if row_index == 0>
						<thead><tr><#list row as c><th>${(c!)?html}</th></#list></tr></thead>
					<#else>
						<#if row_index == 1><tbody></#if>
						<tr><#list row as c><td>${(c!)?html}</td></#list></tr>
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

	<script type="text/javascript">
		function onPageLoad() {
			$('#sqlexec_sql').focus().keyup(function(evt) {
				if (evt.ctrlKey && evt.which == 13) {
					$('#sqlexec_submit').click();
				}
			});
		}
	</script>
</div>

</body>
</html>
