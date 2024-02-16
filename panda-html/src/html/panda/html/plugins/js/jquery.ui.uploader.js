(function($) {
	"use strict";

	var isAdvancedUpload = function() {
		var div = document.createElement('div');
		return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
	}();

	var UNITS = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
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
		var u = fn.lastIndexOf('/'),
			w = fn.lastIndexOf('\\'),
			i = u > w ? u : w;
		return fn.substr(i + 1);
	}

	function _filetype(t, e) {
		if (t) {
			var i = t.indexOf('/'), c = (i >= 0) ? t.slice(0, i) : t;
			return ($.inArray(c, ['image', 'audio', 'video', 'file']) >= 0) ? c : 'file';
		}
		if (e) {
			e = e.toLowerCase();
			if ($.inArray(e, ['.jpg', '.jpeg', '.gif', '.png', '.tif', '.tiff', '.svg', '.bmp', '.webp']) >= 0) {
				return 'image';
			}
			if ($.inArray(e, ['.mp3', '.flac', '.weba', '.wav', '.mid', '.oga', '.wma']) >= 0) {
				return 'audio';
			}
			if ($.inArray(e, ['.avi', '.mpg', '.mpeg', '.mp4', '.m4v', 'mov', '.webm', '.wmv']) >= 0) {
				return 'video';
			}
		}
		return 'file';
	}

	function _item_on_remove() {
		$(this).closest('.ui-uploader-item').removeData('file').fadeOut(function() {
			$(this).remove();
		});
		return false;
	}

	function _create_item($u, f) {
		var uc = $u.data('uploader'),
			fnm = _filename(f.name || f.path || f.id),
			fsz = f.size;

		var $fit = $('<div>', { 'class': 'ui-uploader-item' }).data('file', f);

		$('<input', { type: 'hidden', name: uc.name, 'class': 'ui-uploader-fid' }).appendTo($fit);
		$('<i>', { 'class': 'ui-close' }).click(_item_on_remove).appendTo($fit);

		$('<span>', { 'class': 'ui-uploader-info' })
			.append($('<i>', { 'class': uc.cssIcons.waiting + ' ui-uploader-icon' }))
			.append($('<span>', { 'class': 'ui-uploader-text' }).text(fnm + ' ' + _filesize(fsz)))
			.appendTo($fit);

		$u.find('.ui-uploader-items').prepend($fit);

		return $fit;
	}

	function _update_item($fit, fi) {
		var $u = $fit.closest('.ui-uploader'),
			uc = $u.data('uploader'),
			fid = fi.id || fi.path || fi.name,
			fnm = _filename(fi.name || fi.path || fi.id),
			fsz = fi.size,
			fct = _filetype(fi.type, fi.ext);

		$fit.find('.ui-uploader-fid').val(fid || '');

		var durl;
		if (uc.dnloadUrl && fid) {
			durl = uc.dnloadUrl.replace(uc.dnloadHolder, uc.dnloadEncode ? encodeURIComponent(fid) : fid);
		}

		$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons[fct] + ' ui-uploader-icon');

		if (fnm) {
			$fit.find('.ui-uploader-text').text(fnm + ' ' + _filesize(fsz));
			if (durl) {
				var $fif = $fit.find('.ui-uploader-info');
				$('<a>', { href: durl }).append($fif).appendTo($fit);
			}
		}

		if (uc.dnloadView && durl && fct == 'image') {
			var $fim = $('<div>').addClass('ui-uploader-image').appendTo($fit);
			$('<a>', { href: durl })
				.append($('<img>', { src: durl }))
				.appendTo($fim)
				.fadeIn();
		}
	}

	function _item_progress($fit, p) {
		if (p < 100) {
			var uc = $fit.closest('.ui-uploader').data('uploader');
			$fit.css('background', 'linear-gradient(to right, ' + uc.pgbarFgcolor + + ' ' + p + '%, ' + uc.pgbarBgcolor + ' ' + (100 - p) + '%)');
		} else {
			$fit.css('background', '').addClass('blinking');
		}
	}

	function _ajaxDone(d) {
		if (d) {
			var r = d.result || d.file;
			if (r) {
				_update_item($(this), r);
			}
		}
	}

	function _ajaxFail(xhr, status, err) {
		var $e = $('<div class="ui-uploader-error">');

		if (xhr.responseJSON) {
			$e.addClass('json').text(JSON.stringify(xhr.responseJSON, null, 4));
		} else if (xhr.responseText) {
			$e.html(xhr.responseText);
		} else {
			$e.text(err || status || 'Server error!');
		}

		$(this).append($e);
	}

	function _init($u, uc) {
		$u.addClass('ui-uploader').data('uploader', uc);

		var uploads = [],
			$uf = $u.find('.ui-uploader-file'),
			$ub = $u.find('.ui-uploader-btn'),
			$us = $u.find('.ui-uploader-items');

		if ($us.length < 1) {
			$us = $('<div class="ui-uploader-items"></div>');
			$u.append($us);
		}

		uc.name ||= $uf.attr('name');
		uc.uploadName ||= $uf.attr('name') || uc.name;

		function __start_upload($fit) {
			var f = $fit.data('file');
			if (!f) {
				return;
			}

			$fit.addClass('loading');
			$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons.loading + ' ui-uploader-icon');

			$u.trigger('upload.uploader', { item: $fit, file: f });

			$fit.find('.ui-close').hide();

			var data = {};
			$u.find('.ui-uploader-data').each(function() {
				var $i = $(this);
				data[$i.attr('name')] = $i.val();
			});
			$.extend(data, uc.uploadData);

			var file = {}; file[uc.uploadName] = f;

			$.ajaf({
				url: uc.uploadUrl,
				data: data,
				file: file,
				dataType: 'json',
				uprogress: function(loaded, total) {
					_item_progress($fit, Math.round(loaded * 100 / total));
				},
				success: function(data, status, xhr) {
					$fit.css('background', '').addClass('success');
					uc.ajaxDone.call($fit, data, status, xhr);
					$u.trigger('uploaded.uploader', { item: $fit, data: data });
				},
				error: function(xhr, status, e) {
					$fit.addClass('error');
					$fit.find('.ui-uploader-icon').prop('className', uc.cssIcons['error'] + ' ui-uploader-icon');
					uc.ajaxFail.call($fit, xhr, status, e);
				},
				complete: function() {
					$fit.removeClass('loading blinking').removeData('file');
					if (uc.uploadRemover) {
						$fit.find('.ui-close').show();
					}
					if (!$u.find('.ui-uploader-file').prop('multiple')) {
						$ub.prop('disabled', false);
						$uf.prop('disabled', false);
					}
					__proc_uploads();
				}
			});
		}

		function __proc_uploads() {
			while (uploads.length > 0 && $u.find('.ui-uploader-item.loading').length < uc.uploadLimit) {
				__start_upload(uploads.shift());
			}
		}

		function __append_uploads(f) {
			if (!$u.find('.ui-uploader-file').prop('multiple')) {
				$u.find('.ui-uploader-item').remove();
				$ub.prop('disabled', true);
				$uf.prop('disabled', true);
			}

			var ufs = [];
			if (f instanceof FileList) {
				$.each(f, function(i, f) {
					ufs.push(_create_item($u, f));
				});
			} else if (f instanceof File) {
				ufs.push(_create_item($u, f));
			} else {
				$.each(f.prop('files'), function(i, f) {
					ufs.push(_create_item($u, f));
				});
			}

			uploads = uploads.concat(ufs.reverse());
			__proc_uploads();
		}

		function __file_on_change() {
			if ($uf.val() == "") {
				return;
			}

			__append_uploads($uf);
		}

		function __file_on_drop(e) {
			e.preventDefault();

			if (!$uf.prop('disabled')) {
				var fs = e.originalEvent.dataTransfer.files;
				if (fs.length) {
					__append_uploads($uf.prop('multiple') ? fs : fs.item(0));
				}
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
		var ds = ['uploadData'],
			fs = ['ajaxDone', 'ajaxFail'],
			bs = ['dnloadEncode'],
			ps = [
				'name',
				'uploadUrl',
				'uploadName',
				'uploadLimit',
				'uploadRemover',
				'dnloadUrl',
				'dnloadHolder',
				'dnloadView',
				'pgbarFgcolor',
				'pgbarBgcolor'
			],
			ks = [].concat(ds, fs, bs, ps);

		var c = {};
		$.each(ks, function(i, k) {
			var v = $u.data(k);
			if (v) {
				if ($.inArray(k, ds) >= 0) {
					if (typeof (v) == 'string') {
						try {
							v = JSON.parse(v);
						} catch (e) {
							return;
						}
					}
				} else if ($.inArray(k, fs) >= 0) {
					v = new Function(v);
				} else if ($.inArray(k, bs) >= 0) {
					v = (v === 'true');
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
			// max concurrent upload files
			uploadLimit: 1,

			// show uploader remover icon
			uploadRemover: false,

			// download file id/name placeholder
			dnloadHolder: '$',

			// show image download view
			dnloadView: false,

			// fontawesome4 css
			cssIcons: {
				file: 'fa fa-file-o',
				image: 'fa fa-file-image-o',
				audio: 'fa fa-file-audio-o',
				video: 'fa fa-file-video-o',
				error: 'fa fa-exclamation-circle',
				waiting: 'fa fa-refresh',
				loading: 'fa fa-refresh fa-spin'
			},

			pgbarFgcolor: '#ccc',
			pgbarBgcolor: 'transparent',

			ajaxDone: _ajaxDone,
			ajaxFail: _ajaxFail
		}
	};

	$.fn.uploader = function(c) {
		return this.each(function() {
			var $u = $(this), uc = $u.data('uploader');
			if (uc) {
				$.extend(uc, c);
				return;
			}

			uc = $.extend({}, $.uploader.defaults, _options($u), c);
			_init($u, uc);
		});
	};

	// UPLOADER DATA-API
	// ==================
	$(window).on('load', function() {
		$('[data-spy="uploader"]').uploader();
	});

})(jQuery);
