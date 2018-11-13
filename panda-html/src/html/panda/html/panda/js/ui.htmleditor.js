(function($) {	
	function initSummerNote() {
		if (typeof($.fn.summernote) == 'undefined') {
			setTimeout(initSummerNote, 100);
			return;
		}

		$('textarea.p-htmleditor').each(function() {
			var $t = $(this);
			if ($t.attr('readonly')) {
				$t.summernote({ toolbar: false }).summernote('disable');
				$t.parent().addClass('p-htmleditor-readonly');
			}
			else {
				$t.summernote({
					toolbar: [
						[ 'style', [ 'style', 'fontname', 'fontsize', 'color', 'forecolor', 'backcolor', 'height', 'paragraph', 'ol', 'ul', 'hr' ] ],
						[ 'text', [ 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript' ] ],
						[ 'media', [ 'link', 'picture', 'video', 'table' ] ],
						[ 'edit', [ 'undo', 'redo', 'clear', 'fullscreen', 'codeview', 'help' ] ],
					]
				});
			}
		});
	}

	$(window).on('load', function () {
		var $hs = $('textarea.p-htmleditor');
		if ($hs.size()) {
			var css = $hs.data('summernoteCss');
			if (css) {
				$.jcss(css);
			}
			var js = $hs.data('summernoteJs');
			if (js) {
				$.jscript(js, true);
			}
		
			initSummerNote();
		}
	});
})(jQuery);
