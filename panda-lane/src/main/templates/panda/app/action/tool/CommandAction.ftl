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

	<@p.form id="cmdexec" method="get" theme="simple" action="+/xml" onsubmit="return cmdExecute();">
	<table width="100%">
		<tr><td>Command: (Ctrl+Enter to execute)</td>
			<td align="right">
				<label>Wait (s): </label> <@p.select name="wait" list=[60, 120, 180, 240, 300]/>
			</td>
		</tr>
		<tr><td colspan="2"><@p.textarea name="cmd" rows="8" cssStyle="width:100%" enterfire="#cmdexec_submit"/></td></tr>
		<tr><td colspan="2" align="center"><@p.submit id="cmdexec_submit" icon="flash" label="#(btn-exec)"/></td></tr>
	</table>
	</@p.form>

	<div id="cmd_result"></div>

	<script type="text/javascript">
		function cmdSetResult(s) {
			var $or = $('#cmd_result');
			if (s) {
				$or.html("<hr/><pre>" + s.escapeHtml() + "</pre>");
			}
			else {
				$or.empty();
			}
		}

		function cmdExecute() {
			cmdSetResult();

			if ($('#cmdexec_cmd').val().strip().isEmpty()) {
				return false;
			}
			
			var $o = $('#cmdexec').loadmask("Executing...");
			$.ajax({
				url: $o.attr('action'),
				data: $o.serializeArray(),
				dataType: 'text',
				success: function(data, ts, xhr) {
					cmdSetResult(data.prettifyXml());
				},
				error: function(xhr, ts, err) {
					cmdSetResult(err + '\r\n' + xhr.responseText);
				},
				complete: function(xhr, ts) {
					$o.unloadmask();
				}
			});
			return false;
		}
	</script>
</div>

</body>
</html>
