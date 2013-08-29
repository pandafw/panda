/*
 * This file is part of Pandaw.
 * Copyright(C) 2013 Pandaw Develop Team.
 *
 * Pandaw is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 * 
 * Pandaw is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pandaw. If not, see <http://www.gnu.org/licenses/>.
 */
if (typeof(pw) == "undefined") { pw = {}; }

pw.upload = function(id) {
	var id = id;
	var $u = $('#' + id);
	var pua = $u.attr('uploadAction');
	var pup = $u.attr('uploadParam');
	var pda = $u.attr('dnloadAction');
	var pdp = $u.attr('dnloadParam');
	var pdl = $u.attr('defaultLink');
	var pdt = $u.attr('defaultText');
	
	var $uf = $u.children('.n-uploader-file');
	var $up = $u.children('.n-uploader-progress');
	var $ue = $u.children('.n-uploader-error');
	var $ui = $u.children('.n-uploader-image');
	var $ut = $u.children('.n-uploader-text');

	var $uct = $u.children('.n-uploader-ct');
	var $ufn = $u.children('.n-uploader-fn');
	var $ufs = $u.children('.n-uploader-fs');
	var $usn = $u.children('.n-uploader-sn');
	
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
				$ut.html('<a class="n-a n-a-it" href="' + pda + '?' + pdp + '=' + encodeURIComponent(usn) + '">'
						+ '<em class="ui-icon ui-icon-check n-a-icon n-uploader-icon"></em>'
						+ ufn + ' ' + _filesize(ufs)
						+ '</a>');
			}
			else {
				$ut.html('<span><em class="ui-icon ui-icon-check n-a-icon n-uploader-icon"></em>'
						+ ufn + ' ' + _filesize(ufs)
						+ '</span>');
			}
			$ut.show();
		}
		else if (pdl) {
			$ut.html('<a class="n-a'
					+ (String.isEmpty(pdt) ? ' n-a-io' : ' n-a-it')
					+ '" href="' + pdl + '">'
					+ '<em class="n-icon '
					+ (uct.startsWith('image') ? 'n-icon-file_img' : 'n-icon-attach')
					+ ' n-a-icon n-uploader-icon"></em>'
					+ String.defaults(pdt)
					+ '</a>')
				.show();
		}

		if (usn && uct.startsWith('image')) {
			$ui.html('<img src="' + pda + '?' + pdp + '=' + usn + '"></img>').fadeIn();
		}
		else if (pdl && uct.startsWith('image')) {
			$ui.html('<img src="' + pdl + '"></img>').fadeIn();
		}
	}
	
	function _error(uct, ufn, ufs, usn) {
		$ut.html('<span><em class="ui-icon ui-icon-close n-uploader-icon"></em>'
			+ ((ufn || $uf.val()) + ' ' + _filesize(ufs))
			+ '</span>')
			.show();
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

		$up.css({
			width: $uf.width() + 'px',
			height: Math.floor($uf.height() * 0.8) + 'px'
		})
		.show()
		.progressbar('value', progress);

		var timer = setInterval(function() {
			$up.progressbar('value', progress++);
			if (progress >= 90) {
				if (timer) {
					clearInterval(timer);
					timer = null;
				}
			}
		}, 20);

		function _endUpload() {
			$uf = $u.children('.n-uploader-file');
			progress = 100;
			$up.progressbar('value', progress).css({display: 'none'});
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
					pw.notice({ container: $ue }).add(d);
					$ue.slideDown();
				}
			},
			error: function(xhr, status, e) {
				_endUpload();
				pw.notice({ container: $ue }).add(
						(e ? "<pre>" + (e + "").escapeHtml() + "</pre>" : (xhr ? xhr.responseText : status)),
						'error'
					);
				$ue.slideDown();
			}
		});
	}

	_info();
	$up.hide().progressbar();
	$uf.change(function() { 
		setTimeout(_upload, 10); 
	});
};
