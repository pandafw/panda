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

<div class="n-sect a-cronjobs">
	<div class="p-header">
		<h3><i class="fa fa-clock-o"></i> <@p.text name="title"/></h3>
	</div>

	<#include "/panda/mvc/view/action-alert.ftl"/>

<#if result?has_content>
	<#list result as ce>
	<div class="cron-div">
	<table class="cron-tb">
		<tr><th class="cron-th-no" rowspan="4">${ce_index + 1}.</th>
			<th class="cron-th-url"><@p.text name="label-url"/>:</th>
			<td class="cron-td-url">${base}${(ce.getUrl()!'')?html}</td>
		</tr>
		<tr><th class="cron-th-desc"><@p.text name="label-description"/>:</th>
			<td class="cron-td-desc">${(ce.description!'')?html}</td>
		</tr>
		<tr><th class="cron-th-sche"><@p.text name="label-schedule"/>:</th>
			<td class="cron-td-sche">
				<#if ce.cron??>${ce.cron?html}
				<#elseif ce.fixedDelay gt 0>FixedDelay: ${ce.fixedDelay}, InitialDelay: ${ce.initialDelay}
				<#elseif ce.fixedRate gt 0>FixedRate: ${ce.fixedRate}, InitialDelay: ${ce.initialDelay}
				<#elseif ce.delay gt 0>Delay: ${ce.delay}
				</#if>
			</td>
		</tr>
		<tr><th class="cron-th-result">
				<@p.b btype="primary" icon="icon-execute" onclick="return cronJobExec(this);" text="#(button-exec)"/>
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

			try {
				$d.loadmask();
				$r.html((new Date()).format('yyyy-MM-dd HH:mm:ss'));

				$.ajax({
					url: $a.text(),
					dataType: 'text',
					success: function(data, ts, xhr) {
						$r.html($r.html() 
							+ '<pre class="alert alert-info">' + data.prettifyXml().escapeHtml() + '</pre>');
					},
					error: function(xhr, ts, err) {
						$r.html($r.html()
							+ '<pre class="alert alert-danger">'
							+ ((err ? (err + '\r\n') : '') + xhr.responseText).prettifyXml().escapeHtml()
							+ '</pre>');
					},
					complete: function(xhr, ts) {
						$d.unloadmask();
					}
				});
			}
			catch (ex) {
				$r.html((new Date()).format('yyyy-MM-dd HH:mm:ss') 
					+ '<pre class="alert alert-danger">' + ('ERROR:\r\n' + ex).escapeHtml() + '</pre>');
			}
			return false;
		}
	</script>
</#if>
</div>

</body>
</html>
