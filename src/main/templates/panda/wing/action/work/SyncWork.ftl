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
	<#include "/panda/mvc/view/action-alert.ftl"/>
	
	<div>
		<@p.submit icon="fa-cog" btype="primary" id="btnStart" onclick="doStart()" label="#(button-start)"/>

		<@p.submit icon="fa-stop" btype="danger" id="btnStop" onclick="doStop()" label="#(button-stop)"/>
	</div>
	
	<hr/>

	<div id="syncw_log"></div>
	<br/>

	<script type="text/javascript">
		var pms = [];

		function doStop() {
			$.ajaf({
				url: "<@p.url action='+/stop'/>",
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
			$('#btnStart').disable(true).removeClass("btn-primary").addClass("btn-warning")
				.find('i').addClass('fa-spin');
		}
		function workOnStop() {
			$('#btnStart').disable(false).removeClass("btn-warning").addClass("btn-primary")
				.find('i').removeClass('fa-spin');
		}

		function doStatus() {
			workOnStart();
			$.ajaf({
				url: "<@p.url action='+/status'/>",
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
			doWork("<@p.url action='+/start'/>");
			return false;
		}

		function doWork(url) {
			workOnStart();
			$('#syncw_log').empty();
			
			pms = [];
			var data = {
				'e.onStart': 'parent.work_start("%{msg}");',
				'e.onStatus': 'parent.work_status("%{level}", "%{msg}", "%{count}", "%{total}");',
				'e.onFinish': 'parent.work_finish("%{msg}");'
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
		function work_status(level, msg, count, total) { 
			print_status((new Date()).format('yyyy-MM-dd HH:mm:ss.SSS'), level, msg, count, total); 
		}
		function work_info(m) { 
			work_status('i', m);
		}
		function work_error(m) {
			work_status('e', m);
		}
		function work_success(m) {
			work_status('s', m);
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
		
		function onPageLoad() { doStatus(); }
	</script>
</div>

</body>
</html>
