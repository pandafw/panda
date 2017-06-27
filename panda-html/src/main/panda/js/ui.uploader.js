(function($) {
	var puploader = function($u) {
		if ($u.data('puploader')) {
			return;
		}
		$u.data('puploader', true);
		
		var loading = false;
		
		var $uf = $u.children('.p-uploader-file');
		var $ub = $u.children('.p-uploader-btn');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertAfter($ub.length > 0 ? $ub : $uf);

		var $ue = $('<div class="p-uploader-error"></div>');
		$ue.insertAfter($up);
		
		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _info(fi) {
			var uid = fi.id, ufn = fi.name, ufs = fi.size, uct = fi.contentType;
			var pdl = $u.data('dnloadLink');
			var pdn = $u.data('dnloadName');
			var pdd = JSON.sparse($u.data('dnloadData'));
			var pel = $u.data('defaultLink');
			var pet = $u.data('defaultText');

			var $uitem = $('<div>').addClass('p-uploader-item').appendTo($u);
			var $uid = $('<input>').attr('type', 'hidden').attr('name', $u.data('name')).addClass('p-uploader-fid').appendTo($uitem);
			var $ut = $('<span>').addClass('p-uploader-text').appendTo($uitem);
			var $ui = $('<div>').addClass('p-uploader-image').appendTo($uitem);


			$uid.val(uid || '');
			
			ufn = ufn || uid || $uf.val();
			var durl = null;
			if (pdl && uid) {
				var ps = $.extend({}, pdd);
				ps[pdn] = uid;
				durl = pdl + '?' + $.param(ps);
			}
			
			if (ufn) {
				var s = '<i class="fa fa-paperclip p-uploader-icon"></i> ' + ufn + ' ' + _filesize(ufs)
				if (durl) {
					$ut.html('<a href="' + durl + '">' + s + '</a>');
				}
				else {
					$ut.html('<span>' + s + '</span>');
				}
			}
			else if (pel) {
				$ut.html('<a href="' + pel + '">'
						+ '<i class="fa '
						+ (String.startsWith(uct, 'image/') ? 'fa-picture-o' : 'fa-paperclip')
						+ ' p-uploader-icon"></i> '
						+ String.defaults(pet)
						+ '</a>');
			}
			
			$('<i class="p-uploader-remove fa fa-remove"></i>').click(_remove).appendTo($ut);

			if (String.startsWith(uct, 'image/')) {
				var u = durl || pel;
				if (u) {
					var $a = $('<a>').attr('href', u);
					var $i = $('<img>').addClass('img-thumbnail').attr('src', u).appendTo($a);
					$a.appendTo($ui).fadeIn();
				}
			}
		}

		function _progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _remove() {
			$(this).closest('.p-uploader-item').fadeOut(function() { $(this).remove(); });
			$ue.hide().empty();
		}
		
		function _upload() {
			if (loading || $uf.val() == "") {
				return;
			}
			loading = true;

			$ub.length ? $ub.hide() : $uf.hide();
			$ue.hide().empty();

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
				$uf.val("");
				$ub.length ? $ub.show() : $uf.show();
				loading = false;
			}

			var file = {}; file[$u.data('uploadName')] = $uf; 
			$.ajaf({
				url: $u.data('uploadLink'),
				data: JSON.sparse($u.data('uploadData')),
				file: file,
				dataType: 'json',
				success: function(d) {
					_endUpload();
					if (d.success) {
						$u.children('.p-uploader-item').remove();
						
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
		
		$ub.click(function(e) {
			e.preventDefault();
			$uf.trigger('click');
			return false;
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
