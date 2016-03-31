(function($) {
	var puploader = function($u) {
		var pul = $u.data('uploadLink');
		var pun = $u.data('uploadName');
		var pud = JSON.sparse($u.data('uploadData'));
		var pdl = $u.data('dnloadLink');
		var pdn = $u.data('dnloadName');
		var pdd = JSON.sparse($u.data('dnloadData'));
		var pel = $u.data('defaultLink');
		var pet = $u.data('defaultText');
		
		var $uid = $u.children('.p-uploader-fid');
		var $uf = $u.children('.p-uploader-file');
		var $ut = $u.children('.p-uploader-text');
		var $ui = $u.children('.p-uploader-image');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertBefore($ut);

		var $ue = $('<div class="p-uploader-error"></div>');
		$ue.insertAfter($ut);
		
		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _info(uid, ufn, ufs, uct) {
			$uid.val(uid || '');
			
			ufn = ufn || uid || $uf.val();

			if (ufn) {
				if (uid) {
					var ps = $.extend({}, pdd);
					ps[pdn] = uid;
					var url = pdl + '?' + $.param(ps);
					$ut.html('<a href="' + url + '">'
							+ '<i class="fa fa-check p-uploader-icon"></i> '
							+ ufn + ' ' + _filesize(ufs)
							+ '</a>');
				}
				else {
					$ut.html('<span><i class="fa fa-check p-uploader-icon"></i>'
							+ ufn + ' ' + _filesize(ufs)
							+ '</span>');
				}
				$ut.show();
			}
			else if (pel) {
				$ut.html('<a href="' + pel + '">'
						+ '<i class="fa '
						+ (String.startsWith(uct, 'image/') ? 'fa-picture-o' : 'fa-paperclip')
						+ ' p-uploader-icon"></i> '
						+ String.defaults(pet)
						+ '</a>')
					.show();
			}
			
			$('<i class="p-uploader-remove fa fa-remove"></i>').click(_clear).appendTo($ut);

			if (String.startsWith(uct, 'image/')) {
				if (pdl && uid) {
					var ps = $.extend({}, pdd);
					ps[pdn] = uid;
					var url = pdl + '?' + $.param(ps);
					$ui.html('<img class="img-thumbnail" src="' + url + '"></img>').fadeIn();
				}
				else if (pel) {
					$ui.html('<img class="img-thumbnail" src="' + pel + '"></img>').fadeIn();
				}
			}
		}
		
		function _error(uid, ufn, ufs, uct) {
			$ut.html('<span><i class="fa fa-times-circle p-uploader-icon"></i>'
				+ ((ufn || $uf.val()) + ' ' + _filesize(ufs))
				+ '</span>')
				.show();
		}
		
		function _progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _clear() {
			$uid.val('');
			$uf.val('');
			$ut.html("&nbsp;");
			$ui.empty();
			$ue.hide().empty();
		}
		
		function _upload() {
			$uid.val('');
			$uf.hide();
			$ui.hide().empty();
			$ut.hide().html("&nbsp;");
			$ue.hide().empty();

//			$up.css({
//				width: $uf.width() + 'px',
//				height: Math.floor($uf.height() * 0.8) + 'px'
//			})
			$up.show();

			var progress = 0;
			
			_progress(progress);

			var timer = setInterval(function() {
				_progress(progress++);
				if (progress >= 90) {
					if (timer) {
						clearInterval(timer);
						timer = null;
					}
				}
			}, 20);

			function _endUpload() {
				_progress(100);
				$up.hide();
				$uf.val("").show();
			}

			var file = {}; file[pun] = $uf; 
			$.ajaf({
				url: pul,
				data: pud,
				file: file,
				dataType: 'json',
				success: function(d) {
					_endUpload();
					var r = d.result;
					if (d.success) {
						_info(r.id, r.name, r.size, r.contentType);
					}
					else {
						_error(r.id, r.name, r.size, r.contentType);
						$ue.palert('add', d);
						$ue.slideDown();
					}
				},
				error: function(xhr, status, e) {
					_endUpload();
					$ue.palert('error', (e ? (e + "").escapePhtml() : (xhr ? xhr.responseText : status)));
					$ue.slideDown();
				}
			});
		}

		$uf.change(function() { 
			setTimeout(_upload, 10); 
		});
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
