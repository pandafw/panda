(function($) {
	var isAdvancedUpload = function() {
		var div = document.createElement('div');
		return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
	}();

	var puploader = function($u) {
		if ($u.data('puploader')) {
			return;
		}
		$u.data('puploader', true);
		
		var loading = false;
		var progress = 0;

		var $uf = $u.children('.p-uploader-file');
		var $ub = $u.children('.p-uploader-btn');
		var $us = $u.children('.p-uploader-sep');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertAfter($ub.length > 0 ? $ub : $uf);

		var $ue = $('<div class="p-uploader-error"></div>');
		$ue.insertAfter($up);

		if ($us.length) {
			$us = $('<div class="p-uploader-sep"></div>').insertAfter($ue);
		}

		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _info(fi) {
			var fid = fi.id, fnm = fi.name, fsz = fi.size, fct = fi.type;
			var pdl = $u.data('dnloadLink');
			var pdn = $u.data('dnloadName');
			var pdd = JSON.sparse($u.data('dnloadData'));

			var $fit = $('<div>').addClass('p-uploader-item').insertAfter($us);
			var $fid = $('<input>').attr('type', 'hidden').attr('name', $u.data('name')).addClass('p-uploader-fid').appendTo($fit);
			var $ftx = $('<span>').addClass('p-uploader-text').appendTo($fit);
			var $fim = $('<div>').addClass('p-uploader-image').appendTo($fit);


			$fid.val(fid || '');
			
			fnm = fnm || fid || $uf.val();
			var durl = null;
			if (pdl && fid) {
				var ps = $.extend({}, pdd);
				ps[pdn] = fid;
				durl = pdl + '?' + $.param(ps);
			}
			
			var img = String.startsWith(fct, 'image/');
			if (fnm) {
				var s = '<i class="fa fa-' + (img ? 'image' : 'paperclip') + ' p-uploader-icon"></i> ' + fnm + ' ' + _filesize(fsz);
				if (durl) {
					$('<a>').attr('href', durl).html(s).appendTo($ftx);
				}
				else {
					$('<span>').html(s).appendTo($ftx);
				}
			}
			
			$('<i>').addClass('p-uploader-remove fa fa-remove').click(_remove).appendTo($ftx);

			if (img && durl) {
				var $a = $('<a>').attr('href', durl);
				var $i = $('<img>').addClass('img-thumbnail').attr('src', durl).appendTo($a);
				$a.appendTo($fim).fadeIn();
			}
		}

		function _progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _remove() {
			$(this).closest('.p-uploader-item').fadeOut(function() { $(this).remove(); });
			$ue.hide().empty();
		}

		function _start_upload() {
			loading = true;
			progress = 0;

			$ub.length ? $ub.hide() : $uf.hide();
			$ue.hide().empty();

			$up.show();

			_do_progress();
		}

		function _do_progress() {
			_progress(progress++);
			if (progress < 90) {
				setTimeout(_do_progress, 20);
			}
		}
		
		function _end_upload() {
			_progress(100);
			$up.hide();
			$uf.val("");
			$ub.length ? $ub.show() : $uf.show();
			loading = false;
		}

		function _upload_on_success(d) {
			_end_upload();

			if (d.success) {
				if (!$uf.attr('multiple')) {
					$u.children('.p-uploader-item').remove();
				}
				
				var r = d.result;
				if ($.isArray(r)) {
					for (var i = 0; i < r.length; i++) {
						_info(r[i]);
					}
				}
				else {
					_info(r);
				}
			}
			else {
				if (d.alerts) {
					$ue.palert('add', d.alerts);
				}
				if (d.exception) {
					var e = d.exception;
					$ue.palert('error', (e.message + (e.stackTrace ? ("\n" + e.stackTrace) : "")).escapePhtml());
				}
				$ue.slideDown();
			}
		}

		function _upload_on_error(xhr, status, e) {
			_end_upload();
			$ue.palert('error', (e ? (e + "").escapePhtml() : (xhr ? xhr.responseText : status)));
			$ue.slideDown();
		}
		
		function _upload_on_change() {
			if (loading || $uf.val() == "") {
				return;
			}

			if (isAdvancedUpload && $uf[0].files) {
				_ajax_upload($uf[0].files);
			}
			else {
				_ajaf_upload();
			}
		}

		function _upload_on_drop(e) {
			e.preventDefault();
			if (loading) {
				return;
			}
			_ajax_upload(e.originalEvent.dataTransfer.files);
		}

		function _ajaf_upload() {
			_start_upload();

			var file = {}; file[$u.data('uploadName')] = $uf; 
			$.ajaf({
				url: $u.data('uploadLink'),
				data: JSON.sparse($u.data('uploadData')),
				file: file,
				dataType: 'json',
				success: _upload_on_success,
				error: _upload_on_error
			});
		}

		function _ajax_upload(dfs) {
			_start_upload();

			var data = new FormData();

			var ud = JSON.sparse($u.data('uploadData'));
			if (ud) {
				for (var i in ud) {
					data.append(i, ud[i]);
				}
			}

			if (dfs) {
				$.each(dfs, function(i, f) {
					data.append($u.data('uploadName'), f);
				});
			}

			$.ajax({
				url: $u.data('uploadLink'),
				type: 'POST',
				data: data,
				dataType: 'json',
				cache: false,
				contentType: false,
				processData: false,
				success: _upload_on_success,
				error: _upload_on_error
			});
		}

		// event handler
		$uf.change(function() { 
			setTimeout(_upload_on_change, 10);
		});
		
		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
		});

		// drap & drop
		if (isAdvancedUpload) {
			$u.addClass('p-uploader-draggable')
				.on('drag dragstart dragend dragover dragenter dragleave drop', function(e) {
					e.preventDefault();
					e.stopPropagation();
				})
				.on('dragover dragenter', function() {
					$u.addClass('p-uploader-dragover');
				})
				.on('dragleave dragend drop', function() {
					$u.removeClass('p-uploader-dragover');
				})
				.on('drop', _upload_on_drop);
		}
	};

	// UPLOADER FUNCTION
	// ==================
	$.fn.puploader = function(c) {
		return this.each(function() {
			puploader($(this));
		});
	};
	
	// UPLOADER DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="puploader"]').puploader();
	});
})(jQuery);
