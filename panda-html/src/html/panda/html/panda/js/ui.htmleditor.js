(function($) {
	var langs = {};
	
	function initSummerNotePlugins() {
		$.extend($.summernote.plugins, {
			/**
			 * @param {Object}
			 *            context - context object has status of editor.
			 */
			'media': function (context) {
				// ui has renders to build ui elements.
				// - you can create a button with `ui.button`
				var ui = $.summernote.ui;
				var $n = context.$note;

				// add plugin button
				context.memo('button.media', function() {
					function popup_callback(ms) {
						$.popup().hide();
						if (ms) {
							for (var i = 0; i < ms.length; i++) {
								$n.summernote('insertImage', ms[i].href, ms[i].name);
							}
						}
					}

					// create button
					var button = ui.button({
						contents: '<i class="' + ($n.data('mediaIcon') || 'fa fa-list-alt') + '"/>',
						tooltip: $n.data('mediaText') || 'Media Browser',
						click: function() {
							var url = $n.data('mediaHref');
							var id = $n.data('mediaPopupId');
							if (!id) {
								id = ($n.attr('id') || $n.attr('name') || url.hashCode()) + '_media_popup';
							}
							$.popup({
									id: id,
									url: url
								})
								.show({
									id: id,
									trigger: this,
									popover: true,
									onpopup: null,
									onhide: null,
									callback: popup_callback
								});
						}
					});

					// create jQuery object from button instance.
					return button.render();
				});

				// This methods will be called when editor is destroyed by $('..').summernote('destroy');
				// You should remove elements on `initialize`.
				this.destroy = function() {
					this.$panel.remove();
					this.$panel = null;
				};
			}
		});
	}
	
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

		initSummerNotePlugins();
		
		$('textarea.p-htmleditor.p-summernote').each(function() {
			var $t = $(this);
			var o = {};
			var p = [ 'toolbar', 'popover', 'fontNames', 'fontNamesIgnoreCheck' ];
			for (var i = 0; i < p.length; i++) {
				var v = $t.attr(p[i]);
				if (v) {
					try {
						o[p[i]] = JSON.parse(v);
					}
					catch (e) {
					}
				}
			}
			$.extend(o, $t.data('summernoteOptions'));

			var l = $t.data('summernoteLang');
			if (l) {
				o.lang = l;
			}

			if ($t.attr('readonly')) {
				$t.parent().addClass('p-htmleditor-readonly');
				$t.summernote($.extend(o, { toolbar: false })).summernote('disable');
			}
			else {
				var ms = [ 'picture', 'video' ];
				if ($t.data('mediaHref')) {
					ms.push('media');
				}
				$t.summernote($.extend({
					height: $t.attr('height'),
					followingToolbar: $t.attr('followingToolbar') || false,
					toolbar: [
						[ 'style', [ 'style', 'fontname', 'fontsize', 'color' ] ],
						[ 'text', [ 'bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript' ] ],
						[ 'para', [ /*'height', */'paragraph', 'ol', 'ul' ] ],
						[ 'insert', [ 'hr', 'table', 'link' ] ],
						[ 'media', ms ],
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
