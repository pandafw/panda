(function($) {
	"use strict";

	var isAdvancedUpload = function() {
		var div = document.createElement('div');
		return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
	}();

	var UNITS = [ "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" ];
	function _filesize(n, p) {
		var i = 0, l = UNITS.length - 1;
		while (n >= 1024 && i < l) {
			n = n / 1024
			i++
		}

		p = Math.pow(10, p || 2);
		return '(' + Math.round(n * p) / p + ' ' + UNITS[i] + ')';
	}

	function _filename(fn) {
		var u = fn.lastIndexOf('/');
		var w = fn.lastIndexOf('\\');
		var i = u > w ? u : w;
		return fn.substr(i + 1);
	}
	
	function _filetype(s) {
		var i = s.indexOf('/');
		return (i >= 0) ? s.slice(0, i) : s;
	}

	function _show_item($u, fi) {
		if (!fi) {
			return;
		}

		var uc = $u.data('uploader'),
			fid = fi.id || fi.path || fi.name,
			fnm = _filename(fi.name || fi.path || fi.id),
			fsz = fi.size,
			fct = _filetype(fi.type || '');

		var $fit = $('<div>').addClass('ui-uploader-item').insertAfter($u.children('.ui-uploader-sep')),
			$fid = $('<input>').attr('type', 'hidden').attr('name', uc.name).addClass('ui-uploader-fid').appendTo($fit),
			$ftx = $('<span>').addClass('ui-uploader-text').appendTo($fit),
			$fim = $('<div>').addClass('ui-uploader-image').appendTo($fit);

		$fid.val(fid || '');
		
		fnm = fnm || fid || $u.children('.ui-upload-file').val();
		var durl;
		if (uc.dnloadUrl && fid) {
			durl = uc.dnloadUrl.replace(uc.dnloadHolder, encodeURIComponent(fid));
		}
		
		if (fnm) {
			var ii = uc.cssIcons[fct] || uc.cssIcons['file'];
			var s = '<i class="' + ii + ' ui-uploader-icon"></i> ' + fnm + ' ' + _filesize(fsz);
			if (durl) {
				$('<a>').attr('href', durl).html(s).appendTo($ftx);
			} else {
				$('<span>').html(s).appendTo($ftx);
			}
		}
		
		$ftx.append($('<i>').addClass('ui-uploader-remove fa fa-remove').click(function() {
			$(this).closest('.ui-uploader-item').fadeOut(function() {
				$(this).remove();
			});
			$u.find('.ui-uploader-error').hide().empty();
		}));

		if (durl && fct == 'image') {
			$('<a>', { href: durl })
				.append($('<img>', { src: durl }))
				.appendTo($fim)
				.fadeIn();
		}
	}

	function _ajaxDone(d) {
		if (d) {
			var r = d.result || d.files, $u = $(this);
			if (r && !$u.children('.ui-uploader-file').prop('multiple')) {
				$u.children('.ui-uploader-item').remove();
			}

			if ($.isArray(r)) {
				for (var i = 0; i < r.length; i++) {
					_show_item($u, r[i]);
				}
			} else {
				_show_item($u, r);
			}
		}
	}

	function _ajaxFail(xhr, status, e) {
		$(this).children('.ui-uploader-error')
			.empty()
			.text(e ? (e + "") : (xhr ? xhr.responseText : status))
			.show();
	}
	
	function _init($u, uc) {
		$u.addClass('ui-uploader').data('uploader', uc);

		var loading = false,
			$uf = $u.children('.ui-uploader-file'),
			$ub = $u.children('.ui-uploader-btn'),
			$ue = $u.children('.ui-uploader-error'),
			$us = $u.children('.ui-uploader-sep'),
			$up = $('<div class="ui-uploader-progress" style="display: none">')
				.addClass(uc.cssProgress)
				.append($('<div class="ui-uploader-progressbar" style="width: 0%">').addClass(uc.cssProgressBar))
				.insertAfter($ub.length > 0 ? $ub : $uf);

		if ($ue.length < 1) {
			$ue = $('<div class="ui-uploader-error"></div>').insertAfter($up);
		}
		$ue.hide();
		
		if ($us.length < 1) {
			$us = $('<div class="ui-uploader-sep"></div>').insertAfter($ue);
		}

		uc.name ||= $uf.attr('name');
		uc.uploadName ||= $uf.attr('name') || uc.name;

		// functions
		function _set_progress(v) {
			$up.children('.ui-uploader-progressbar').css({width: v + '%'});
		}
		
		function __start_upload() {
			loading = true;
	
			($ub.length ? $ub : $uf).hide();
			$ue.hide().empty();
	
			_set_progress(0);
			$up.show();
		}
	
		function __end_upload() {
			loading = false;
	
			$up.hide();
			_set_progress(0);

			$uf.val("");
			($ub.length ? $ub : $uf).show();
		}
	
		function __upload_on_progress(loaded, total) {
			var p = Math.round(loaded * 100 / total);
			_set_progress(p);
		}
		
		function __upload_on_success(data, status, xhr) {
			uc.ajaxDone.call($u, data, status, xhr);
			$u.trigger('uploaded.uploader', data);
		}

		function __upload_on_error(xhr, status, e) {
			uc.ajaxFail.call($u, xhr, status, e);
		}

		function __ajaf_upload(file) {
			__start_upload();
	
			$.ajaf({
				url: uc.uploadUrl,
				data: uc.uploadData,
				file: file,
				dataType: 'json',
				forceAjaf: uc.forceAjaf,
				progress: __upload_on_progress,
				success: __upload_on_success,
				error: __upload_on_error,
				complete: __end_upload
			});
		}
	
		function __file_on_change() {
			if (loading || $uf.val() == "") {
				return;
			}
	
			var f = {}; f[uc.uploadName] = $uf;
			__ajaf_upload(f);
		}
	
		function __file_on_drop(e) {
			e.preventDefault();
			if (loading) {
				return;
			}

			var fs = e.originalEvent.dataTransfer.files;
			if (fs.length) {
				var f = {}; f[uc.uploadName] = $uf.prop('multiple') ? fs : fs.item(0);
				__ajaf_upload(f);
			}
		}
	
		// event handler
		$uf.change(function() {
			setTimeout(__file_on_change, 10);
		});
		
		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
		});

		// drap & drop
		if (isAdvancedUpload) {
			$u.addClass('ui-uploader-draggable')
				.on('drag dragstart dragend dragover dragenter dragleave drop', function(e) {
					e.preventDefault();
					e.stopPropagation();
				})
				.on('dragover dragenter', function() {
					$u.addClass('ui-uploader-dragover');
				})
				.on('dragleave dragend drop', function() {
					$u.removeClass('ui-uploader-dragover');
				})
				.on('drop', __file_on_drop);
		}
	}

	function _options($u) {
		var ks = [
			'name',
			'forceAjaf',
			'uploadUrl',
			'uploadName',
			'dnloadUrl',
			'dnloadHolder'
		];
		var ds = ['uploadData'];
		var fs = ['ajaxDone', 'ajaxFail'];

		var c = {};
		$.each(ks, function(i, k) {
			var v = $u.data(k);
			if (v) {
				if ($.inArray(k, ds) >= 0) {
					if (typeof(v) == 'string') {
						try {
							v = JSON.parse(v);
						} catch (e) {
							return;
						}
					}
				} else if ($.inArray(k, fs) >= 0) {
					v = new Function(v);
				}
				c[k] = v;
			}
		});
		return c;
	}

	// UPLOADER FUNCTION
	// ==================
	$.uploader = {
		defaults: {
			forceAjaf: false,
			dnloadHolder: '$',

			// bootstrap3/4 css
			cssProgress: 'progress',
			cssProgressBar: 'progress-bar progress-bar-info progress-bar-striped',

			// fontawesome4 css
			cssIcons: {
				image: 'fa fa-file-image-o',
				video: 'fa fa-file-video-o',
				file: 'fa fa-clip'
			},

			ajaxDone: _ajaxDone,
			ajaxFail: _ajaxFail
		}
	};

	$.fn.uploader = function(c) {
		return this.each(function() {
			var $u = $(this);
			if ($u.data('uploader')) {
				return;
			}
	
			_init($u, $.extend({}, $.uploader.defaults, _options($u), c));
		});
	};
	
	// UPLOADER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="uploader"]').uploader();
	});

})(jQuery);
