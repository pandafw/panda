<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_pwhash">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>

	<div id="pwhash_alert">
		<#include "/action-alert.ftl"/>
	</div>

	<@p.form id="pwhash" method="post" theme="simple" action="+/hash" onsubmit="return doHash();">
	<table width="100%">
		<tr><td>Password: (Ctrl+Enter to hash)</td></tr>
		<tr><td><@p.textarea name="txt" rows="8" cssStyle="width:100%" enterfire="form"/></td></tr>
		<tr><td align="center"><@p.submit id="pwhash_submit" icon="flash">Hash</@p.submit></td></tr>
	</table>
	</@p.form>

	<br/>
	<br/>

	<div id="pwhash_result" class="table-responsive" style="display:none;">
	<table class="table table-striped table-bordered p-fz80p p-th-nowrap ">
		<thead><tr><th>Password</th><th>Hash</th></tr></thead>
		<tbody>
		</tbody>
	</table>
	</div>

	<script type="text/javascript">
		function doHash() {
			if ($('#pwhash_txt').val().isEmpty()) {
				return false;
			}
			
			$('#pwhash_alert').empty();
			$('#pwhash_result').find('tbody').empty().end().hide();

			var $o = $('#pwhash').loadmask("Hashing...");
			$.ajax({
				url: $o.attr('action'),
				method: 'post',
				data: $o.serializeArray(),
				dataType: 'json',
				success: function(d) {
					if (!d.success) {
						$('#pwhash_alert').palert('actionError', d);
						return;
					}
	
					if (d.result) {
						$.each(d.result, function() {
							var $tr = $('<tr>');
							$tr.append($('<td>').text(this.key));
							$tr.append($('<td>').text(this.value));
							$('#pwhash_result tbody').append($tr);
						});
						$('#pwhash_result').show();
					}
				},
				error: function(xhr, status, e) {
					$('#pwhash_alert').palert('ajaxJsonError', xhr, status, e, "<@p.text name='error-server-connect' escape='js'/>");
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
