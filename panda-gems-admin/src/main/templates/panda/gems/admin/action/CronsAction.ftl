<html>
<head>
	<title><@p.text name="title"/></title>
	<style type="text/css">
		.cron-tb {
			width: 100%;
		}
		.cron-tb th {
			font-weight: normal;
			vertical-align: top;
		}
		.cron-tb td {
			vertical-align: top;
		}
		.cron-th-no {
			width: 20px;
		}
		.cron-th-url, .cron-th-desc, .cron-th-sche, .cron-th-result {
			padding: 0 10px;
			width: 100px;
			white-space: nowrap;
		}
		.cron-td-url, .cron-td-desc, .cron-td-sche, .cron-td-result {
			width: 99%;
		}
		.cron-td-result {
			color: #008;
		}
	</style>
</head>

<body>

<div class="p-section a_cronjobs">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>

	<#include "/action-alert.ftl"/>

<#if result?has_content>
	<#list result as ce>
	<div class="cron-div">
	<table class="cron-tb">
		<tr><th class="cron-th-no" rowspan="4">${ce_index + 1}.</th>
			<th class="cron-th-url"><@p.text name="lbl-url"/>:</th>
			<td class="cron-td-url">${base}${(ce.action!'')?html}</td>
		</tr>
		<tr><th class="cron-th-desc"><@p.text name="lbl-description"/>:</th>
			<td class="cron-td-desc">${(ce.description!'')?html}</td>
		</tr>
		<tr><th class="cron-th-sche"><@p.text name="lbl-schedule"/>:</th>
			<td class="cron-td-sche">
				<#if ce.cron??>${ce.cron?html}
				<#elseif ce.fixedDelay gt 0>FixedDelay: ${ce.fixedDelay}, InitialDelay: ${ce.initialDelay}
				<#elseif ce.fixedRate gt 0>FixedRate: ${ce.fixedRate}, InitialDelay: ${ce.initialDelay}
				<#elseif ce.delay gt 0>Delay: ${ce.delay}
				</#if>
			</td>
		</tr>
		<tr><th class="cron-th-result">
				<@p.b btype="primary" icon="icon-execute" onclick="return cronJobExec(this);" label="#(btn-exec)"/>
			</th>
			<td class="cron-td-result"></td>
		</tr>
	</table>
	</div>
	<hr/>
	</#list>

	<script type="text/javascript">
		function cronJobExec(el) {
			var $d = $(el).closest('div.cron-div');
			var $a = $d.find('.cron-td-url');
			var $r = $d.find('.cron-td-result');

			$d.loadmask();

			$r.html((new Date()).format('yyyy-MM-dd HH:mm:ss'));
			$.ajax({
				url: $a.text(),
				dataType: 'text',
				success: function(data, ts, xhr) {
					$r.html($r.html() 
						+ '<pre class="alert alert-info">' + data.escapeHtml() + '</pre>');
				},
				error: function(xhr, ts, err) {
					$r.html($r.html()
						+ '<pre class="alert alert-danger">'
						+ ((err ? (err + '\r\n') : '') + xhr.responseText).escapeHtml()
						+ '</pre>');
				},
				complete: function(xhr, ts) {
					$d.unloadmask();
				}
			});
			return false;
		}
	</script>
</#if>
</div>

</body>
</html>
