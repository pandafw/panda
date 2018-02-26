<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_eleval">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="pwdhash" method="post" theme="simple">
	<table width="100%">
		<tr><td>Password: (Ctrl+Enter to hash)</td></tr>
		<tr><td><@p.textarea name="txt" rows="8" cssStyle="width:100%"/></td></tr>
		<tr><td align="center"><@p.submit id="pwdhash_submit" icon="flash">Hash</@p.submit></td></tr>
	</table>
	</@p.form>

<#if result??>
	<div class="table-responsive">
	<table class="table">
		<thead><tr><th>Password</th><th>Hash</th></tr></thead>
		<tbody>
<#list result as r>
			<tr><td>${r.key?html}</td>
				<td>${r.value?html}</td>
			</tr>
</#list>
		</tbody>
	</table>
	</div>
</#if>

	<script type="text/javascript">
		function onPageLoad() {
			$('#pwdhash_txt').focus().keyup(function(evt) {
				if (evt.ctrlKey && evt.which == 13) {
					$('#pwdhash_submit').click();
				}
			});
		}
	</script>
</div>

</body>
</html>
