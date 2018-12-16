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
#media_uploader {
	display: none;
	margin: 10px 0;
}
#media_tool {
	display: inline-block;
	font-size: 150%;
	margin: 0 20px;
	vertical-align: middle;
}
#media_tool a {
	margin: 0 10px;
}
#media_tool_trash {
	display: none;
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
			label="#(lbl-date)"
			name="ds"
			maxlength="10"
			size="10"
		/>
		 ~ 
		<@p.datepicker
			name="de"
			maxlength="10"
			size="10"
			/>
		
		<div id="media_tool">
			<@p.a id="media_tool_upload" icon="upload" tooltip="#(btn-upload)"/>
			<@p.a id="media_tool_trash" icon="trash" tooltip="#(btn-delete)"/>
		</div>
		
		<@p.textfield
			cssClass="media-search"
			name="qs"
			size="18"
			ricon="search"
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
	
	<#if result?has_content>
		<div id="media_browser">
		<#list result as _m>
			<a data-mid="${_m.id}" class="media-thumb img-thumbnail" href="#" title="${_m.name?html}<br>
${_m.width!} x ${_m.height} (<@p.number value=_m.size format="size"/>)<br>
<@p.date value=_m.createdAt format="datetime"/>
">
				<img src="thumb?id=${_m.id}"/>
			</a>
		</#list>
			<div id="media_end"></div>
		</div>
	</#if>
</div>

<script>
	var media_limit = ${a.mediaLimit};
	var media_date_format = null;
	var media_has_next = false;
	var media_loading = false;
	
	function onPageLoad() {
		media_date_format = new DateFormat("<@p.text name='date-format-datetime'/>");

		media_has_next = ($('#media_browser a.media-thumb').size() >= media_limit);
		$('#media_tool_upload').click(function(e) {
			e.preventDefault();
			$('#media_uploader').slideToggle();
		});
		$('#media_tool_trash').click(function(e) {
			e.preventDefault();
		});
		$('#media_form').submit(media_on_change);
		$('#media_form div.p-datepicker').on('changeDate', media_on_change);
		$('#meida_form_ds, #media_form_de, #media_form_qs').change(media_on_change);
		$('#media_browser a.media-thumb')
			.tooltip({placement: 'auto', html: true})
			.click(media_on_click);

		$(window).scroll(media_on_scroll);
	}

	function media_on_click() {
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
	
	function media_search(last) {
		var ps = $('#media_form').serializeArray();
		if (last) {
			ps.push({ name: 'sn', value: last });
		}
		
		media_loading = true;
		$('#media_alert').palert('clear');
		$('#media_browser').loadmask();
		$.ajax({
			url: 'browse',
			data: ps,
			dataType: 'json',
			success: function(d) {
				if (!d.success) {
					media_action_error(d);
					return;
				}

				if (!last) {
					$('#media_browser').find('.media-thumb').remove();
				}
				
				if (d.result) {
					$.each(d.result, function() {
						media_append(this);
					});
				}
				media_has_next = d.result ? (d.result.length >= media_limit) : false;
			},
			error: meia_ajax_error,
			complete: function() {
				media_loading = false;
				$('#media_browser').unloadmask();
			}
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
	
	function meia_ajax_error(xhr, status, e) {
		$('#media_alert').palert('ajaxJsonError', xhr, status, e, "<@p.text name='error-server-connect' escape='js'/>");
	}
</script>
</body>
</html>
