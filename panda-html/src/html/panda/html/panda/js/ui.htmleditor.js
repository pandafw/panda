(function($) {
	var langs = {};
	function initSummerNote() {
		if (typeof($.fn.summernote) == 'undefined') {
			setTimeout(initSummerNote, 100);
			return;
		}
		for (var i in langs) {
			if (!$.summernote.lang[i]) {
				setTimeout(initSummerNote, 100);
				return;
			}
		}

		$('textarea.p-htmleditor.p-summernote').each(function() {
			var $t = $(this);
			var o = $t.data('summernoteOptions') || {};
			var l = $t.data('summernoteLang');
			if (l) {
				o.lang = l;
			}

			if ($t.attr('readonly')) {
				$t.parent().addClass('p-htmleditor-readonly');
				$t.summernote($.extend(o, { toolbar: false })).summernote('disable');
			}
			else {
				$t.summernote($.extend({
					followingToolbar: false,
					toolbar: [
						[ 'style', [ 'style', 'fontname', 'fontsize', 'color' ] ],
						[ 'text', [ 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript' ] ],
						[ 'para', [ /*'height', */'paragraph', 'ol', 'ul', 'hr', 'table', 'link' ] ],
						[ 'media', [ 'picture', 'video' ] ],
						[ 'edit', [ 'undo', 'redo', 'clear' ] ], 
						[ 'misc', [ 'codeview', 'fullscreen', 'help' ] ],
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
				$.jscript(js);
			}
			$sns.each(function() {
				var i = $(this).data('summernoteLang');
				var v = $(this).data('summernoteLangJs');
				if (i && v && !langs[i]) {
					$.jscript(v);
					langs[i] = v;
				}
			});
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
