<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<style>
	#shell_result pre {
		border: 0 none;
		padding: 0;
		margin: 0;
		background: transparent;
	}
</style>

<div class="p-section" id="a_shell">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/> (<@p.text name="%{'panda.lang.Systems'@OS_NAME}"/>)</h3>
	</div>

	<div id="shell_alert">
		<#include "/action-alert.ftl"/>
	</div>

	<@p.form id="shell" method="post" theme="simple" action="+/exec.json" onsubmit="return shellExecute();">
	<table width="100%">
		<tr><td>Command: (Ctrl+Enter to execute)</td>
			<td align="right">
				<label>Wait (s): </label> <@p.select name="wait" list=[60, 120, 180, 240, 300]/>
			</td>
		</tr>
		<tr><td colspan="2"><@p.textarea name="cmd" rows="8" cssStyle="width:100%" enterfire="#shell_submit"/></td></tr>
		<tr><td colspan="2" align="center"><@p.submit id="shell_submit" icon="flash" label="#(btn-exec)"/></td></tr>
	</table>
	</@p.form>

	<br/>
	<br/>

	<div id="shell_result" class="table-responsive" style="display:none;">
		<table class="table table-striped table-bordered p-th-nowrap">
		<tbody>
		</tbody>
		</table>
	</div>

	<script type="text/javascript">
		function shellExecute() {
			if ($('#shell_cmd').val().strip().isEmpty()) {
				return false;
			}

			$('#shell_alert').empty();
			$('#shell_result').find('tbody').empty().end().hide();
			
			var $o = $('#shell').loadmask("Executing...");
			$.ajax({
				url: $o.attr('action'),
				method: 'post',
				data: $o.serializeArray(),
				dataType: 'json',
				success: function(d) {
					if (!d.success) {
						$('#shell_alert').palert('actionAlert', d);
						return;
					}

					if (d.result) {
						var $srt = $('#shell_result tbody');
						function addResult(d, p) {
							var $tr = $('<tr>');
							$tr.append($('<td>').text(p));
							$tr.append($('<td>').append($('<pre>').text(d[p])));
							$srt.append($tr);
						}
						addResult(d.result, 'code');
						addResult(d.result, 'elapsed');
						addResult(d.result, 'stdout');
						addResult(d.result, 'stderr');
						$('#shell_result').show();
					}
				},
				error: function(xhr, status, err) {
					$('#shell_alert').palert('ajaxJsonError', xhr, status, err, "<@p.text name='error-server-connect' escape='js'/>");
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
