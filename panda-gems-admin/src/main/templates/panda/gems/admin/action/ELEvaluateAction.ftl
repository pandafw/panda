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

	<@p.form id="eleval" method="post" theme="simple" action="+/exec.xml" onsubmit="return elEvaluate();">
	<table width="100%">
		<tr><td>Expression: (Ctrl+Enter to evaluate)</td></tr>
		<tr><td><@p.textarea name="expr" rows="8" cssStyle="width:100%" enterfire="#eleval_submit"/></td></tr>
		<tr><td align="center"><@p.submit id="eleval_submit" icon="flash">Evaluate</@p.submit></td></tr>
	</table>
	</@p.form>

	<div id="eleval_result"></div>

	<script type="text/javascript">
		function elSetResult(s) {
			var $or = $('#eleval_result');
			if (s) {
				$or.html("<hr/><pre>" + s.escapeHtml() + "</pre>");
			}
			else {
				$or.empty();
			}
		}

		function elEvaluate() {
			elSetResult();
			if ($('#eleval_expr').val().strip().isEmpty()) {
				return false;
			}
			
			var $o = $('#eleval').loadmask("Evaluating...");
			$.ajax({
				url: $o.attr('action'),
				method: 'post',
				data: $o.serializeArray(),
				dataType: 'text',
				success: function(data, ts, xhr) {
					elSetResult(data.prettifyXml());
				},
				error: function(xhr, ts, err) {
					elSetResult(err + '\r\n' + xhr.responseText);
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
