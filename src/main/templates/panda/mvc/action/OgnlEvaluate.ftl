<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_ognleval">
	<div class="p-header">
		<h3><i class="fa fa-keyboard-o"></i> <@p.text name="title"/></h3>
	</div>
	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form id="ognleval" method="get" theme="simple" onsubmit="return ognlEvaluate();">
	<table width="100%">
		<tr><td>Expression: (Ctrl+Enter to evaluate)</td></tr>
		<tr><td><@p.textarea name="expr" rows="8" cssStyle="width:100%"/></td></tr>
		<tr><td align="center"><@p.submit id="ognleval_submit" icon="flash">Evaluate</@p.submit></td></tr>
	</table>
	</@p.form>

	<div id="ognleval_result"></div>

	<script type="text/javascript">
		function onPageLoad() {
			$('#ognleval_expr').focus().keyup(function(evt) {
				if (evt.ctrlKey && evt.which == 13) {
					$('#ognleval_submit').click();
				}
			});
		}
		
		function ognlSetResult(s) {
			var $or = $('#ognleval_result');
			if (s) {
				$or.html("<hr/><pre>" + s.escapeHtml() + "</pre>");
			}
			else {
				$or.empty();
			}
		}

		function ognlEvaluate() {
			try {
				ognlSetResult();
				if ($('#ognleval_expr').val().strip().isEmpty()) {
					return false;
				}
				
				var $o = $('#ognleval').loadmask("Evaluating...");
				$.ajax({
					url: $o.attr('action'),
					data: $o.serializeArray(),
					dataType: 'text',
					success: function(data, ts, xhr) {
						ognlSetResult(data.prettifyXml());
					},
					error: function(xhr, ts, err) {
						ognlSetResult(err + '\r\n' + xhr.responseText);
					},
					complete: function(xhr, ts) {
						$o.unloadmask();
					}
				});
			}
			catch (ex) {
				ognlSetResult("ERROR:\r\n" + ex);
			}
			return false;
		}
	</script>
</div>

</body>
</html>
