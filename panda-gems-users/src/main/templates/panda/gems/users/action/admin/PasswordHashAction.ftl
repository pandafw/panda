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
			
			var $a = $('#pwhash_alert').empty(),
				$r = $('#pwhash_result').find('tbody').empty().end().hide(),
				$f = $('#pwhash').loadmask("Hashing...");

			$.ajax({
				url: $f.attr('action'),
				method: 'post',
				data: $f.serializeArray(),
				dataType: 'json',
				success: function(d) {
					if (!d.success) {
						$a.palert('ajaxDataAlert', d);
						return;
					}
	
					if (d.result) {
						$.each(d.result, function() {
							var $tr = $('<tr>');
							$tr.append($('<td>').text(this.key));
							$tr.append($('<td>').text(this.value));
							$r.find('tbody').append($tr);
						});
						$r.show();
					}
				},
				error: function(xhr, status, err) {
					$a.palert('ajaxJsonError', xhr, status, err, "<@p.text name='error-server-connect' escape='js'/>");
				},
				complete: function(xhr, ts) {
					$f.unloadmask();
				}
			});
			return false;
		}
	</script>
</div>

</body>
</html>
