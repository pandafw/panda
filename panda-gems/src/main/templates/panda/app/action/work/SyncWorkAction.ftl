<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>
<style>
	#syncw_log {
		max-height: 480px;
		overflow-y: auto;
	}
	#syncw_log div {
		border: none 0;
		font-size: 80%;
		margin: 0;
		padding: 2px;
		white-space: pre-wrap;
	}
	#syncw_log div.error {
		color: #f00;
	}
</style>
<div class="p-section" id="a_syncwork">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>
	
	<@safeinclude path=(a.templatePrefix!('/' + a.class.name?replace('.', '/')) + "-wells.ftl") />

	<@p.form cssClass="p-eform" id="syncw" focusme="false" method="get" theme="bs3h">
		<@safeinclude path=(a.templatePrefix!('/' + a.class.name?replace('.', '/')) + "-inputs.ftl") />
		<@p.div cssClass="p-buttons">
			<@p.submit icon="fa-cog" btype="primary" id="btnStart" onclick="return syncw_start();" label="#(btn-start)"/>
			<@p.submit icon="fa-stop" btype="danger" id="btnStop" onclick="return syncw_stop();" label="#(btn-stop)"/>
		</@p.div>
	</@p.form>
	
	<hr/>

	<div id="syncw_log"></div>
	<br/>

	<script type="text/javascript">
		var work_msg_cls = { 'd': 'bg-primary', 'i': 'bg-info', 'w': 'bg-warning', 'e': 'bg-danger', 's': 'bg-success' };
		var work_msgs_limit = 10000;
		var work_msgs = [];

		function syncw_status() {
			work_on_start();
			$.ajax({
				url: "<@p.url action='+/status' includeParams='get'/>",
				dataType: "json",
				success: function(o) {
					if (!o.success) {
						work_error(data);
						setTimeout(syncw_status, 1000);
						return;
					}

					var r = o.result;
					work_print_status(r.date, r.level, r.status, r.count, r.total);
					if (r.running) {
						setTimeout(syncw_status, 1000);
					}
					else {
						work_on_stop();
					}
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
					setTimeout(syncw_status, 1000);
				}
			});
		}
		
		function syncw_start() {
			work_on_start();

			work_msgs = [];
			$('#syncw_log').empty();
			
			var url = "<@p.url action='+/start' includeParams='get'/>";
			var data = [
				{ 'name': 'e.onStart', 'value': 'parent.work_start("%{msg}");' },
				{ 'name': 'e.onStatus', 'value': 'parent.work_status("%{level}", "%{msg}", "%{count}", "%{total}");' },
				{ 'name': 'e.onFinish', 'value': 'parent.work_finish("%{msg}");' }
			];
			$.merge(data, $('#syncw').serializeArray());
			
			$.ajaf({
				url: url,
				data: data,
				forceAjaf: true,
				success: function(d) {
					work_on_stop();
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
					work_on_stop();
				}
			});

			return false;
		}
		
		function syncw_stop() {
			$.ajax({
				url: "<@p.url action='+/stop' includeParams='get'/>",
				success: function(o) {
					if (!o.success) {
						work_error(data);
						return;
					}
					
					var r = o.result;
					work_print_status(r.date, r.level, r.status, r.count, r.total);
					if (!r.running) {
						work_on_stop();
					}
				},
				error: function(xhr, status, e) {
					work_error(xhr.responseText);
				}
			});
			return false;
		}

		function work_on_start() {
			$('#btnStart').disable(true).removeClass("btn-primary").addClass("btn-warning")
				.find('i').addClass('fa-spin');
		}
		function work_on_stop() {
			$('#btnStart').disable(false).removeClass("btn-warning").addClass("btn-primary")
				.find('i').removeClass('fa-spin');
		}

		function work_start(msg) {
			work_info(msg);
		}
		function work_status(level, msg, count, total) { 
			work_print_status((new Date()).format('yyyy-MM-dd HH:mm:ss.SSS'), level, msg, count, total); 
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
		
		function work_status_msg(time, level, msg, count, total) {
			if (count === undefined || count == 0) {
				return '> ' + time + ' [' + level + '] - ' + msg;
			}

			if (total === undefined || total == 0) {
				return '> ' + time + " [" + level + "] " + count + " - " + msg;
			}

			return "> " + time + " [" + level + "] " + count + '/' + total + " - " + msg;
		}
		
		function work_status_cls(cls) {
			return work_msg_cls[cls || 'i'] || '';
		}

		function work_print_status(time, level, msg, count, total) {
			if (msg) {
				var $m = $('<div>', { 'class': work_status_cls(level) }).text(work_status_msg(time, level, msg, count, total));

				var $sl = $('#syncw_log');
				
				$sl.append($m).scrollTop($sl.scrollTop() + $m.outerHeight() + 20);

				work_msgs.push($m);
				while (work_msgs.length > work_msgs_limit) {
					work_msgs.shift().remove();
				}
			}
		}
		
		function onPageLoad() {
			syncw_status();
		}
	</script>
</div>

</body>
</html>
