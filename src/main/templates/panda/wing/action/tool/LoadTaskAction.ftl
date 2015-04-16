<html>
<head>
	<title><@p.text name="title"/></title>
	<style type="text/css">
		table.mc-table {
			width: 100%;
		}
		table.mc-table th, table.mc-table td {
			padding: 5px;
			vertical-align: top;
		}
		td.mc-td-no {
			width: 30px;
		}
		td.mc-td-name, td.mc-td-key, td.mc-td-time {
			width: 150px;
		}
	</style>
</head>

<body>

<div class="p-section" id="a_cache">
	<div class="p-header">
		<h3><@p.text name="title"/></h3>
	</div>
	<#include "/panda/mvc/view/action-alert.ftl"/>

	<table class="table mc-table">
	<thead>
		<tr>
			<th>No.</th>
			<th>Name</th>
			<th>Key</th>
			<th>Load Time</th>
			<th>Reload</th>
		</tr>
	</thead>
	<tbody>
<#if result?has_content>
	<#list result as c>
		<tr>
			<td class="mc-td-no">${c_index + 1}.</td>
			<td class="mc-td-name"><@p.text name=c/></td>
			<td class="mc-td-key">${c}</td>
			<td class="mc-td-time"><@p.property value=(app[c + '.load.date']!) format="datetime"/></td>
			<td class="mc-td-reload"><@p.a icon="icon-reload" action="/task/${c}/load/start" onclick="return mc_reload(this);"><@p.text name="button-reload"/></@p.a></td>
		</tr>
		<tr style="display:none">
			<td>&nbsp;</td>
			<td colspan="4" class="mc-td-result"></td>
		</tr>
	</#list>
</#if>
	</tbody>
	</table>

	<script type="text/javascript">
		function mc_reload(el) {
			var $a = $(el);
			var $d = $a.closest('tr');
			var $r = $d.next().show().children('.mc-td-result');

			try {
				$a.parent().loadmask();
				$r.html((new Date()).format('yyyy-MM-dd HH:mm:ss'));

				$.ajax({
					url: $a.attr('href'),
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
</div>

</body>
</html>
