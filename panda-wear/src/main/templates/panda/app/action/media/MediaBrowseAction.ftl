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
	margin: 3px;
}
.media-select {
	background-color: #6495ED;
}
#media_uploader {
	display: none;
	margin: 10px 0;
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
	
	<@p.form id="media_form" action="browse" theme="bs3">
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
			/>
		
		<div id="media_tool" class="form-group">
			<@p.a id="media_btn_upload" btn="default" icon="upload" label="#(btn-upload)"/>
			<@p.a id="media_btn_delete" btn="warning" icon="trash" label="#(btn-delete)" cssClass="p-hidden"/>
		</div>
	</@p.form>

	<@p.uploader
		id="media_uploader"
		name="files"
		accept="image/*,video/*,audio/*"
		size="30"
		multiple="true"
		uploadAction="uploads"
		uploadName="files"
		dataOnUploaded="media_on_uploaded"
	/>
	
	<div id="media_browser">
		<div id="media_end"></div>
	</div>
</div>

<script>
	var media_items = <#if result?has_content>${assist.toJson(result)}<#else>[]</#if>;
	var media_limit = ${a.mediaPageLimit};
	var media_date_format = null;
	var media_has_next = false;
	var media_loading = false;
	
	function onPageLoad() {
		$.jcss('${statics!}/lightbox/jquery.ui.lightbox.<#if !(appDebug!false)>min.</#if>css?v=${assist.appVersion}');
		$.jscript('${statics!}/lightbox/jquery.ui.lightbox.<#if !(appDebug!false)>min.</#if>js?v=${assist.appVersion}');

		media_date_format = new DateFormat("<@p.text name='date-format-datetime'/>");

		media_has_next = (media_items.length >= media_limit);
		$.each(media_items, function() {
			media_append(this);
		});
		media_lightbox();

		$('#media_btn_upload').click(function(e) {
			e.preventDefault();
			$('#media_uploader').slideToggle();
		});
		$('#media_btn_delete').click(function(e) {
			e.preventDefault();
			media_delete();
		});
		$('#media_form').submit(media_on_change);
		$('#media_form div.p-datepicker').on('changeDate', media_on_change);
		$('#meida_form_ds, #media_form_de, #media_form_qs').change(media_on_change);

		$(window).scroll(media_on_scroll);
	}

	function media_lightbox() {
		if (typeof($.fn.lightbox) != 'function') {
			setTimeout(media_lightbox, 100);
			return;
		}
		$('#media_browser .media-thumb').lightbox({bindEvent: 'dblclick contextmenu'});
	}
	
	function media_on_click(e) {
		e.preventDefault();
		$(this).toggleClass('media-select');
		$('#media_btn_delete')[$('#media_browser .media-select').size() ? 'removeClass' : 'addClass']('p-hidden');
		return false;
	}
	
	function media_title(m) {
		return m.name 
			+ '\r\n' + m.width + ' x ' + m.height
			+ ' (' + String.formatSize(m.size) + ')'
			+ '\r\n' + media_date_format.format(new Date(m.createdAt));
	}

	function media_append(m) {
		media_create(m).insertBefore('#media_end');
	}

	function media_prepend(m) {
		media_create(m).prependTo('#media_browser');
	}

	function media_create(m) {
		return $('<a class="media-thumb img-thumbnail" href="#">')
			.append($('<img src="thumb/' + m.id + '"/>').attr('alt', m.name))
			.attr({
				'href': 'media/' + m.id,
				'title': media_title(m)
			})
			.data('mid', m.id)
			.click(media_on_click);
	}
	
	function media_on_uploaded(d) {
		var r = d.result;
		if ($.isArray(r)) {
			for (var i = 0; i < r.length; i++) {
				media_prepend(r[i]);
			}
		}
		else {
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
	
	function media_delete() {
		var ps = [];
		$('#media_browser .media-select').each(function() {
			ps.push({name: 'id', value: $(this).data('mid')});
		});
		
		media_ajax_start();
		$.ajax({
			url: 'deletes',
			method: 'POST',
			data: ps,
			dataType: 'json',
			success: function(d) {
				if (!d.success) {
					media_action_error(d);
					return;
				}

				$('#media_browser .media-select').remove();
				$('#media_btn_delete').addClass('p-hidden');
			},
			error: media_ajax_error,
			complete: media_ajax_end
		});
	}
	
	function media_search(last) {
		var ps = $('#media_form').serializeArray();
		if (last) {
			ps.push({ name: 'sn', value: last });
		}
		
		media_ajax_start();
		$.ajax({
			url: 'browse',
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
			var sn = $('#media_browser a.media-thumb:last').data('mid');
			media_search(sn);
		}
	}

	function media_action_error(d) {
		$('#media_alert').palert('actionError', d);
	}
	
	function media_ajax_error(xhr, status, e) {
		$('#media_alert').palert('ajaxJsonError', xhr, status, e, "<@p.text name='error-server-connect' escape='js'/>");
	}
</script>
</body>
</html>
