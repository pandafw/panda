(function($) {
	function initSummerNote() {
		if (typeof($.fn.summernote) == 'undefined') {
			setTimeout(initSummerNote, 100);
			return;
		}

		$('textarea.p-htmleditor.p-summernote').each(function() {
			var $t = $(this);
			var o = $t.data('summernoteOptions') || {};
			if ($t.attr('readonly')) {
				$t.parent().addClass('p-htmleditor-readonly');
				$t.summernote($.extend(o, { toolbar: false })).summernote('disable');
			}
			else {
				$t.summernote($.extend({
					toolbar: [
						[ 'style', [ 'style', 'fontname', 'fontsize', 'color', 'forecolor', 'backcolor', 'height', 'paragraph', 'ol', 'ul', 'hr' ] ],
						[ 'text', [ 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript' ] ],
						[ 'media', [ 'link', 'picture', 'video', 'table' ] ],
						[ 'edit', [ 'undo', 'redo', 'clear', 'fullscreen', 'codeview', 'help' ] ],
					]
				}, o));
			}
		});
	}

	function initClEditor() {
		if (typeof($.fn.cleditor) == 'undefined') {
			setTimeout(initClEditor, 100);
			return;
		}

		$('textarea.p-htmleditor.p-cleditor').cleditor();
	}

	$(window).on('load', function () {
		var $sns = $('textarea.p-htmleditor.p-summernote');
		if ($sns.size()) {
			var css = $sns.data('summernoteCss');
			if (css) {
				$.jcss(css);
			}
			var js = $sns.data('summernoteJs');
			if (js) {
				$.jscript(js, true);
			}
			initSummerNote();
		}

		var $ces = $('textarea.p-htmleditor.p-cleditor');
		if ($ces.size()) {
			var css = $ces.data('cleditorCss');
			if (css) {
				$.jcss(css);
			}
			var js = $ces.data('cleditorJs');
			if (js) {
				$.jscript(js, true);
			}
			initClEditor();
		}
	});
})(jQuery);
