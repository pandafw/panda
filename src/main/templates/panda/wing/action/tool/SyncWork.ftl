<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>
<style>
	#syncw_log pre {
		border: none 0;
		margin: 0;
		padding: 2px;
	}
	#syncw_log pre.error {
		color: #f00;
	}
</style>
<div class="p-section" id="a_syncwork">
	<div class="p-header">
		<h3><i class="fa fa-cog"></i> <@p.text name="title"/></h3>
	</div>
	<#include "/panda/exts/struts2/views/action-alert.ftl"/>
	
	<div>
		<@p.submit icon="fa-cog" btype="primary" id="btnExec" onclick="doExecute()"><@p.text name="button-execute"/></@p.submit>
		
		<@p.submit icon="fa-stop" btype="danger" id="btnStop" onclick="doStop()"><@p.text name="button-stop"/></@p.submit>
	</div>
	
	<hr/>

	<div id="syncw_log"></div>
	<br/>

	<script type="text/javascript">
		var pms = [];

		function doStop() {
			$.ajaf({
				url: "<@p.url action='${actionPrefix}_stop'/>",
				data: { __decorator: 'none' },
				success: function(data) {
					var o = JSON.parse(data);
					print_status(o.time, o.level, o.status, o.count, o.total);
					if (!o.running) {
						workOnStop();
					}
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
				}
			});
		}
		
		function workOnStart() {
			$('#btnExec').disable(true).removeClass("btn-primary").addClass("btn-warning")
				.find('i').addClass('fa-spin');
		}
		function workOnStop() {
			$('#btnExec').disable(false).removeClass("btn-warning").addClass("btn-primary")
				.find('i').removeClass('fa-spin');
		}

		function doStatus() {
			workOnStart();
			$.ajaf({
				url: "<@p.url action='${actionPrefix}_status'/>",
				data: { __decorator: 'none' },
				success: function(data) {
					var o = JSON.parse(data);
					print_status(o.time, o.level, o.status, o.count, o.total);
					if (o.running) {
						setTimeout(doStatus, 1000);
					}
					else {
						workOnStop();
					}
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
					setTimeout(doStatus, 1000);
				}
			});
		}
		
		function doExecute() {
			doWork("<@p.url action='${actionPrefix}_execute'/>");
			return false;
		}

		function doWork(url) {
			workOnStart();
			$('#syncw_log').empty();
			
			pms = [];
			var data = {
				__decorator: 'none',
				onStart: 'parent.work_start("{0}");',
				onStatus: 'parent.work_status("{0}", "{1}", "{2}", "{3}");',
				onFinish: 'parent.work_finish("{0}");'
			};
			
			$.ajaf({
				url: url,
				data: data,
				success: function(d) {
					workOnStop();
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
					workOnStop();
				}
			});
		}

		function work_start(msg) {
			work_info(msg);
		}
		function work_finish(msg) {
			work_success(msg);
		}
		
		function statusMsg(time, level, msg, count, total) {
			if (count === undefined || count == 0) {
				return '> ' + time + ' [' + level + '] - ' + msg;
			}

			if (total === undefined || total == 0) {
				return '> ' + time + " [" + level + "] " + count + " - " + msg;
			}

			return "> " + time + " [" + level + "] " + count + '/' + total + " - " + msg;
		}
		
		var status_cls = { 'i': 'alert-info', 'w': 'alert-warning', 'e': 'alert-danger', 's': 'alert-success' };
		function statusCls(cls) {
			return status_cls[cls || 'i'] || '';
		}
		function print_status(time, level, msg, count, total) {
			if (msg) {
				var $m = $('<pre class="' + statusCls(level) + '">' + String.escapeHtml(statusMsg(time, level, msg, count, total)) + '</pre>');

				$('#syncw_log').append($m);

				pms.push($m);
				while (pms.length > 20) {
					pms.shift().remove();
				}
			}
		}

		function work_status(level, msg, count, total) { print_status((new Date()).format('yyyy-MM-dd HH:mm:ss.SSS'), level, msg, count, total); }
		function work_info(m) { work_status('i', m); }
		function work_error(m) { work_status('e', m); }
		function work_success(m) { work_status('s', m); }
		
		function onPageLoad() { doStatus(); }
	</script>
</div>

</body>
</html>
