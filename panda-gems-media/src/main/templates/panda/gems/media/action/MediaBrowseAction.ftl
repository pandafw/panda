<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<style>
#media_form .form-group {
	display: inline-block;
	margin-bottom: 10px;
	vertical-align: middle;
}
.media-de.form-group, .media-qs.form-group {
	margin-right: 10px;
}
#media_tool a {
	margin: 0 10px 0 0;
}
.media-thumb {
	display: inline-block;
	vertical-align: top;
	border: 1px solid #ddd;
	margin: 3px;
	padding: 4px;
	width: 138px;
}
.media-thumb>img {
	width: 100%;
	height: auto;
}
.media-select {
	background-color: #6495ED;
}
#media_uploader {
	margin: 10px 0;
	min-height: 400px;
}
#media_uploader.ui-uploader-draggable {
	padding: 0;
	outline: 0;
}
#media_uploader.ui-uploader-dragover {
	outline: 2px dashed #ccc;
	outline-offset: 10px;
}
#media_uploader .ui-uploader-btn, #media_uploader .ui-uploader-item {
	display: none !important;
}
#media_browser {
	margin: 15px 0;
}
</style>

<div class="p-section" id="a_media_browser">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>

<#if text.getText("well", "")?has_content>
	<div class="p-well"><@p.text name="well"/></div>
</#if>

	<div id="media_alert">
		<#include "/action-alert.ftl"/>
	</div>
	
	<@p.form id="media_form" action="./" theme="bs3">
		<@p.datepicker
			cssClass="media-ds"
			name="ds"
			maxlength="10"
			size="10"
			placeholder="#(lbl-date)"
		/>
		 ~ 
		<@p.datepicker
			cssClass="media-de"
			name="de"
			maxlength="10"
			size="10"
			/>
		
		<@p.textfield
			cssClass="media-qs"
			name="qs"
			size="18"
			ricon="search"
			placeholder="#(btn-search)"
			clearholder="true"
			/>
		
		<div id="media_tool" class="form-group">
			<@p.a id="media_btn_upload" btn="default" icon="upload" title="#(btn-upload)"/>
			<@p.a id="media_btn_slides" btn="default" icon="play" title="#(btn-slides)" cssClass="p-hidden"/>
			<@p.a id="media_btn_delete" btn="warning" icon="trash" title="#(btn-delete)" cssClass="p-hidden"/>
			<@p.a id="media_btn_select" btn="primary" icon="select" label="#(btn-select)" cssClass="p-hidden"/>
		</div>
	</@p.form>

	<@p.uploader
		id="media_uploader"
		name="files"
		accept="image/*,video/*,audio/*"
		size="30"
		multiple="true"
		uploadUrl="./uploads"
	>
		<div id="media_browser">
			<div id="media_end"></div>
		</div>
	</@p.uploader>
</div>

<script type="text/javascript">
	var media_active = null;
	var media_method = '${methodName!}';
	var media_base = '';
	var media_has_next = false;
	var media_loading = false;
	var media_limit = ${a.mediaIndexLimit};
	var media_items = <#if result?has_content>${assist.toJson(result, "datetime")}<#else>[]</#if>;

	function onPageLoadMediaBrowse() {
		media_base = $('#media_form').attr('action');

		media_has_next = (media_items.length >= media_limit);
		$.each(media_items, function() {
			media_append(this);
		});
		media_lightbox();

		$('#media_btn_upload').click(function(e) {
			e.preventDefault();
			$('#media_uploader .ui-uploader-btn').trigger('click');
		});
		$('#media_btn_slides').click(function(e) {
			e.preventDefault();
			(media_active || $('#media_browser a.media-thumb:first')).trigger('dblclick');
		});
		$('#media_btn_delete').click(function(e) {
			e.preventDefault();
			media_delete();
		});
		$('#media_btn_select').click(function(e) {
			e.preventDefault();
			media_select();
		});
		$('#media_form').submit(media_on_change);
		$('#media_form div.p-datepicker').on('changeDate', media_on_change);
		$('#media_form_ds, #media_form_de, #media_form_qs').change(media_on_change);

		$('#media_uploader').on('uploaded.uploader', media_on_uploaded);
		$(window).scroll(media_on_scroll);
	}

	function media_lightbox() {
		if (typeof($.fn.lightbox) != 'function') {
			setTimeout(media_lightbox, 100);
			return;
		}
		var $ms = $('#media_browser .media-thumb');
		$ms.lightbox({
			fixedNavigation: ${((userAgent.mobile)!false)?c},
			bindEvent: 'dblclick'
		});
		$('#media_btn_slides')[$ms.length ? 'removeClass' : 'addClass']('p-hidden');
	}
	
	function media_set_method(m) {
		if (media_method != m) {
			$('#media_browser .media-select').removeClass('media-select');
			$('#media_btn_select').addClass('p-hidden');
		}
		media_method = m;
	}
	
	function media_on_click(e) {
		e.preventDefault();

		var $t = $(this);
		if (media_method == 'select_popup') {
			$('#media_btn_select').addClass('p-hidden');
			$('#media_browser .media-select').removeClass('media-select');
			$t.addClass('media-select');
			var s = {
				id: $t.data('mid'),
				slug: $t.data('slug'),
				name: $t.find('img').attr('alt'),
				href: $t.attr('href')
			};
			$.popup().callback(s);
		}
		else {
			if (e.shiftKey) {
				var s = $t.hasClass('media-select') ? 'removeClass' : 'addClass';
				var d = (media_active || $('#media_browser a.media-thumb:first')).data('mid');
				var m = $t.data('mid') > d ? 'next' : 'prev';
				for (var $n = $t; $n.size(); $n = $n[m]()) {
					$n[s]('media-select');
					if ($n.data('mid') == d) {
						break;
					}
				}
			}
			else {
				media_active = $t;
				$t.toggleClass('media-select');
			}

			var i = $('#media_browser .media-select').size() ? 'removeClass' : 'addClass';
			$('#media_btn_delete')[i]('p-hidden');
			if (media_method == 'browse_popup') {
				$('#media_btn_select')[i]('p-hidden');
			}
		}
		return false;
	}
	
	function media_append(m) {
		media_create(m).insertBefore('#media_end');
	}

	function media_prepend(m) {
		media_create(m).prependTo('#media_browser');
	}

	function media_create(m) {
		return $('<a class="media-thumb" href="#">')
			.append($('<img src="' + media_base + 'thumb/128/' + m.slug + '"/>').attr('alt', m.name))
			.attr({
				'href': media_base + 'media/' + m.slug,
				'title': media_title(m)
			})
			.data('mid', m.id)
			.data('slug', m.slug)
			.click(media_on_click);
	}
	
	function media_title(m) {
		return m.name 
			+ '\r\n' + m.width + ' x ' + m.height
			+ ' (' + Number.humanSize(m.size) + ')'
			+ '\r\n' + m.updatedAt;
	}

	function media_on_uploaded(e, d) {
		var r = d.result;
		if ($.isArray(r)) {
			for (var i = 0; i < r.length; i++) {
				media_prepend(r[i]);
			}
		} else {
			media_prepend(r);
		}
		media_lightbox();
		return true;
	}

	function media_on_change() {
		media_search();
	}

	function media_ajax_start() {
		media_loading = true;
		$('#media_alert').palert('clear');
		$('#media_browser').loadmask();
	}

	function media_ajax_end() {
		media_loading = false;
		$('#media_browser').unloadmask();
	}

	function media_select() {
		var ms = [];
		$('#media_browser .media-select').each(function() {
				var $t = $(this);
				ms.push({
					id: $t.data('mid'),
					slug: $t.data('slug'),
					name: $t.find('img').attr('alt'),
					href: $t.attr('href')
				});
			})
			.removeClass('media-select');
		$('#media_btn_delete, #media_btn_select').addClass('p-hidden');
		media_active = null;
		$.popup().callback(ms);
	}
	
	function media_delete() {
		var ps = [];
		$('#media_browser .media-select').each(function() {
			ps.push({name: 'id', value: $(this).data('mid')});
		});
		
		media_ajax_start();
		$.ajax({
			url: media_base + 'deletes',
			method: 'POST',
			data: ps,
			dataType: 'json',
			success: function(d) {
				media_action_error(d);

				if (d.result) {
					$('#media_browser .media-select').each(function() {
						var $t = $(this);
						if (d.result[$t.data('mid')] == true) {
							$t.remove();
						}
					});
				}
				if ($('#media_browser .media-select').size() == 0) {
					$('#media_btn_delete, #media_btn_select').addClass('p-hidden');
				}
				media_active = null;
				media_lightbox();
			},
			error: media_ajax_error,
			complete: media_ajax_end
		});
	}
	
	function media_search(last) {
		var ps = $('#media_form').serializeArray();
		if (last) {
			ps.push({ name: 'si', value: last });
		}
		
		media_ajax_start();
		$.ajax({
			url: media_base + 'browse',
			method: 'POST',
			data: ps,
			dataType: 'json',
			success: function(d) {
				if (!d.success) {
					media_action_error(d);
					return;
				}

				if (!last) {
					$('#media_browser .media-thumb').remove();
				}

				if (d.result) {
					$.each(d.result, function() {
						media_append(this);
					});
					media_lightbox();
				}
				media_has_next = d.result ? (d.result.length >= media_limit) : false;
			},
			error: media_ajax_error,
			complete: media_ajax_end
		});
	}
	
	function media_on_scroll() {
		var $w = $(window);
		if (media_has_next && !media_loading && $w.scrollTop() + $w.height() > $("#media_end").offset().top) {
			var mid = $('#media_browser a.media-thumb:last').data('mid');
			media_search(mid);
		}
	}

	function media_action_error(d) {
		$('#media_alert').palert('ajaxDataAlert', d);
	}
	
	function media_ajax_error(xhr, status, err) {
		$('#media_alert').palert('ajaxJsonError', xhr, status, err, "<@p.text name='error-server-connect' escape='js'/>");
	}
</script>
</body>
</html>
