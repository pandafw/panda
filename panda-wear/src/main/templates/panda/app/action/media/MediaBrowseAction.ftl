<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<style>
.media-search.form-group {
	float: right;
	margin: 0 5px;
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
#media_tool {
	display: inline-block;
	margin: 0 10px;
	vertical-align: middle;
}
#media_tool a {
	margin: 0 5px;
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
	
	<@p.form id="media_form" action="browse" theme="bs3i">
		<@p.datepicker
			name="ds"
			maxlength="10"
			size="10"
			placeholder="#(lbl-date)"
		/>
		 ~ 
		<@p.datepicker
			name="de"
			maxlength="10"
			size="10"
			/>
		
		<div id="media_tool">
			<@p.a id="media_tool_upload" btn="default" icon="upload" label="#(btn-upload)"/>
			<@p.a id="media_tool_delete" btn="warning" icon="trash" label="#(btn-delete)" cssClass="p-hidden"/>
		</div>
		
		<@p.textfield
			cssClass="media-search"
			name="qs"
			size="18"
			ricon="search"
			placeholder="#(btn-search)"
			/>
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
		media_date_format = new DateFormat("<@p.text name='date-format-datetime'/>");

		$.each(media_items, function() {
			media_append(this);
		});
		media_has_next = (media_items.length >= media_limit);

		$('#media_tool_upload').click(function(e) {
			e.preventDefault();
			$('#media_uploader').slideToggle();
		});
		$('#media_tool_delete').click(function(e) {
			e.preventDefault();
			media_delete();
		});
		$('#media_form').submit(media_on_change);
		$('#media_form div.p-datepicker').on('changeDate', media_on_change);
		$('#meida_form_ds, #media_form_de, #media_form_qs').change(media_on_change);

		$(window).scroll(media_on_scroll);
	}

	function media_on_click() {
		$(this).toggleClass('media-select');
		$('#media_tool_delete')[$('#media_browser .media-select').size() ? 'removeClass' : 'addClass']('p-hidden');
		return false;
	}
	
	function media_title(m) {
		return m.name 
			+ '<br>' + m.width + ' x ' + m.height
			+ ' (' + String.formatSize(m.size) + ')'
			+ '<br>' + media_date_format.format(new Date(m.createdAt));
	}

	function media_append(m) {
		media_create(m).insertBefore('#media_end');
	}

	function media_prepend(m) {
		media_create(m).prependTo('#media_browser');
	}

	function media_create(m) {
		return $('<a class="media-thumb img-thumbnail" href="#">')
			.append($('<img src="thumb?id=' + m.id + '"/>'))
			.attr('title', media_title(m))
			.data('mid', m.id)
			.tooltip({placement: 'auto', html: true})
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
