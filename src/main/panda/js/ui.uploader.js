(function($) {
	var puploader = function($u) {
		var pua = $u.attr('uploadAction');
		var pup = $u.attr('uploadParam');
		var pda = $u.attr('dnloadAction');
		var pdp = $u.attr('dnloadParam');
		var pdl = $u.attr('defaultLink');
		var pdt = $u.attr('defaultText');
		
		var $uf = $u.children('.p-uploader-file');
		var $ut = $u.children('.p-uploader-text');
		var $ui = $u.children('.p-uploader-image');

		var $uct = $u.children('.p-uploader-ct');
		var $ufn = $u.children('.p-uploader-fn');
		var $ufs = $u.children('.p-uploader-fs');
		var $usn = $u.children('.p-uploader-sn');
		
		var $up = $('<div class="progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertAfter($uf);

		var $ue = $('<div class="p-uploader-error"></div>');
		$ue.insertAfter($ut);
		
		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _info(uct, ufn, ufs, usn) {
			uct = uct || $uct.val();
			ufn = ufn || $ufn.val();
			ufs = ufs || $ufs.val();
			usn = usn || $usn.val();

			if (ufn) {
				if (usn) {
					$ut.html('<a href="' + pda + '?' + pdp + '=' + encodeURIComponent(usn) + '">'
							+ '<i class="fa fa-check p-uploader-icon"></i> '
							+ ufn + ' ' + _filesize(ufs)
							+ '</a>');
				}
				else {
					$ut.html('<span><i class="fa fa-check n-uploader-icon"></i>'
							+ ufn + ' ' + _filesize(ufs)
							+ '</span>');
				}
				$ut.show();
			}
			else if (pdl) {
				$ut.html('<a href="' + pdl + '">'
						+ '<i class="fa '
						+ (uct.startsWith('image') ? 'fa-picture-o' : 'fa-paperclip')
						+ ' p-uploader-icon"></i> '
						+ String.defaults(pdt)
						+ '</a>')
					.show();
			}

			if (usn && uct.startsWith('image')) {
				$ui.html('<img class="img-thumbnail" src="' + pda + '?' + pdp + '=' + usn + '"></img>').fadeIn();
			}
			else if (pdl && uct.startsWith('image')) {
				$ui.html('<img class="img-thumbnail" src="' + pdl + '"></img>').fadeIn();
			}
		}
		
		function _error(uct, ufn, ufs, usn) {
			$ut.html('<span><i class="fa fa-times-circle p-uploader-icon"></i>'
				+ ((ufn || $uf.val()) + ' ' + _filesize(ufs))
				+ '</span>')
				.show();
		}
		
		function _progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _upload() {
			var progress = 0;

			$uct.val('');
			$ufn.val('');
			$ufs.val('');
			$usn.val('');
			
			$ue.hide().empty();
			$ui.hide().empty();
			$ut.hide().empty();
			$uf.hide();		

//			$up.css({
//				width: $uf.width() + 'px',
//				height: Math.floor($uf.height() * 0.8) + 'px'
//			})
			$up.show();
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
				$uf = $u.children('.p-uploader-file');
				_progress(100);
				$up.hide();
				$uf.show();
			}

			var file = {}; file[pup] = $uf; 
			$.ajaf({
				url: pua,
				file: file,
				dataType: 'json',
				success: function(d) {
					_endUpload();
					var r = d[pup];
					if (d.success) {
						$uct.val(r.contentType);
						$ufn.val(r.fileName);
						$ufs.val(r.fileSize);
						$usn.val(r.saveName);
						_info();
					}
					else {
						_error(r.contentType, r.fileName, r.fileSize, r.saveName);
						panda.alert({ container: $ue }).add(d);
						$ue.slideDown();
					}
				},
				error: function(xhr, status, e) {
					_endUpload();
					panda.alert({ container: $ue }).error(
							(e ? "<pre>" + (e + "").escapeHtml() + "</pre>" : (xhr ? xhr.responseText : status))
						);
					$ue.slideDown();
				}
			});
		}

		_info();
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
