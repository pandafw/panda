(function($) {
	function setAlertType($a, s, t) {
		for (var i in s.types) {
			$a.removeClass(s.types[i]);
		}
		$a.addClass(s.types[t]);
	}

	function msg_li(s, t, m, n) {
		if (n && s.label) {
			m = n + s.label + m;
		}
		if (s.escape) {
			m = m.escapeHtml();
		}
		return $('<li>').addClass(s.texts[t])
			.append($('<i>').addClass(s.icons[t]))
			.append($('<span>').html(m));
	}

	function addMsg($a, s, m, t) {
		$a.append($('<ul>').addClass(s.css).append(msg_li(s, t, m)));
		setAlertType($a, s, t);
	}

	function addMsgs($a, s, m, t) {
		if (m) {
			var $u = $('<ul>').addClass(s.css);
			if ($.isArray(m)) {
				for (var i = 0; i < m.length; i++) {
					$u.append(msg_li(s, t, m[i]));
				}
			}
			else {
				for (var n in m) {
					var v = m[n];
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							$u.append(msg_li(s, t, v[i], n));
						}
					}
					else {
						$u.append(msg_li(s, t, v, n));
					}
				}
			}
			$a.append($u);
			setAlertType($a, s, t);
		}
	}

	function addInputErrors($f, s, m, t) {
		if (m) {
			for (var n in m) {
				var $i = $f.find('input[name="' + n + '"]');
				if ($i.length) {
					$i.closest('.form-group').addClass('has-error').find('.p-field-errors').remove();
					var $u = $('<ul>').attr('errorfor', n).addClass(s.css).addClass('p-field-errors');
					var v = m[n];
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							$u.append(msg_li(s, t, v[i], n));
						}
					}
					else {
						$u.append(msg_li(s, t, v, n));
					}
					$u.insertAfter($i);
				}
			}
		}
	}

	function addAlerts($a, s, m, t) {
		if (typeof(m) == 'string') {
			addMsg($a, s, m, t || 'info');
		}
		else if ($.isArray(m)) {
			for (var i = 0; i < m.length; i++) {
				if (typeof(m[i]) == 'string') {
					addMsg($a, s, m[i], t || 'info');
				}
				else {
					addMsg($a, s, m[i].html, m[i].type);
				}
			}
		}
		else if (m) {
			if (m.params) {
				if (t) {
					addInputErrors($(t), s, m.params.errors, 'error');
				}
				else {
					addMsgs($a, s, m.params.errors, "error");
				}
			}
			if (m.action) {
				addMsgs($a, s, m.action.errors, 'error');
				addMsgs($a, s, m.action.warnings, 'warn');
				addMsgs($a, s, m.action.confirms, 'help');
				addMsgs($a, s, m.action.messages, 'info');
			}
		}
	}

	$.palert = {
		css: 'fa-ul',
		label: false, //': ',
		escape: true,
		icons: {
			'down': 'fa-caret-down',
			'up': 'fa-caret-up',
			'info': 'fa-li fa fa-info-circle',
			'help':'fa-li fa fa-question-circle',
			'warn': 'fa-li fa fa-exclamation-triangle',
			'error': 'fa-li fa fa-exclamation-circle'
		},
		texts: {
			'info': 'text-success',
			'help':'text-warning',
			'warn': 'text-warning',
			'error': 'text-danger'
		},
		types: {
			'info': 'alert-success',
			'help':'alert-info',
			'warn': 'alert-warning',
			'error': 'alert-danger'
		}
	};
	
	var palert = function($c, s) {
		if (s) {
			$c.data('palert', s);
		}
		return {
			clear: function() {
				$c.children('.alert').remove();
				return this;
			},
			error: function(m) {
				return this.add(m, 'error');
			},
			warn: function(m) {
				return this.add(m, 'warn');
			},
			help: function(m) {
				return this.add(m, 'help');
			},
			info: function(m) {
				return this.add(m, 'info');
			},
			add: function(m, t) {
				var s = $.extend({}, $c.data('palert'), $.palert);
				var a = false, $a = $c.children('.p-alert');
				if ($a.size() < 1) {
					$a = $('<div></div>').addClass('p-alert alert alert-dismissable fade in').css('display', 'none');
					a = true;
				}

				addAlerts($a, s, m, t);

				if (a && $a.children().length) {
					$a.prepend("<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>");
					$c.prepend($a);
					$a.slideDown();
				}
				return this;
			},
			actionAlert: function(d, f) {
				if (d.alerts) {
					this.add(d.alerts, f);
					if (d.alerts.params && !d.alerts.params.empty) {
						this.error($(f).data('ajaxInputError'));
					}
				}
				if (d.exception) {
					var e = d.exception;
					var m = e.message + (e.stackTrace ? ("\n" + e.stackTrace) : "");
					this.add(m, 'error');
				}
				return this;
			},
			ajaxJsonError: function(xhr, status, e, m) {
				if (xhr && xhr.responseJSON) {
					var d = xhr.responseJSON;
					if (d && (d.alerts || d.exception)) {
						return this.actionAlert(d);
					}
				}
			
				msg = '';
				if (e) {
					msg += e + '\n';
				}
				
				if (xhr && xhr.responseText) {
					try {
						var r = JSON.parse(xhr.responseText);
						msg += JSON.stringify(r, null, 2);
					}
					catch (ex) {
						msg += xhr.responseText;
					}
				}
			
				return this.add(msg || m, 'error');
			}
		}
	};
	
	$.fn.palert = function(opt) {
		var ops = typeof opt === 'object' && opt;
		var args = Array.prototype.slice.call(arguments, 1);
		return this.each(function() {
			var pa = palert($(this), ops);
			if (typeof opt === 'string') {
				pa[opt].apply(pa, args);
			}
		});
	};

	$.palert.notify = function(m, t, s) {
		s = $.extend({}, $.palert, s);
		var ns = $.extend({ style: 'palert' }, $.palert.notifys);

		var $a = $('<div>').addClass('p-alert alert');
		addAlerts($a, s, m, t);
		
		if ($.notify) {
			if (!$.notify.getStyle('palert')) {
				$.notify.addStyle('palert', {
					html: '<div data-notify-html="html"></div>'
				});
			}
			$.notify({ html: $a}, ns);
		}
	};
	
	$.palert.toggleFieldErrors = function(el) {
		var $fes = $(el).closest('.p-field-errors-alert').next('.p-field-errors');
		var id = $.palert.icons.down;
		var iu = $.palert.icons.up;
		if ($fes.is(':hidden')) {
			$fes.slideDown();
			$(el).children('i').removeClass(id).addClass(iu);
		}
		else {
			$fes.slideUp();
			$(el).children('i').removeClass(iu).addClass(id);
		}
		return false;
	};

})(jQuery);
///////////////////////////////////////////////////////
// swipe for carousel
//
(function($) {
	var regTouchGestures = function($e) {
		$e.hammer()
			.on("swipeleft", function(e) {
				$(this).data('bs.carousel').next();
				e.gesture.stopDetect();
			})
			.on("swiperight", function(e) {
				$(this).data('bs.carousel').prev();
				e.gesture.stopDetect();
			});
	};
	
	$(window).on('load', function () {
		if (typeof($.fn.hammer) == 'function') {
			$('[data-ride="carousel"]').each(function() {
				regTouchGestures($(this));
			})
		}
	});
})(jQuery);
(function($) {
	$(window).on('load', function() {
		$(".p-checkboxlist.order")
			.removeClass("order")
			.addClass("ordered")
			.find(":checkbox").click(function() {
				var $t = $(this);
				var $l = $t.closest('label');
				var $h = $t.closest('.p-checkboxlist').find('hr');
				$l.fadeOut(function() {
					if ($t.is(':checked')) {
						$l.insertBefore($h);
					}
					else {
						$l.insertAfter($h);
					}
					$l.fadeIn();
				});
			});
	});
})(jQuery);
(function($) {
	var langs = {};
	var dps = 'div.p-datepicker, div.p-datetimepicker, div.p-timepicker';

	function initDateTimePicker() {
		if (typeof($.fn.datetimepicker) == 'undefined') {
			setTimeout(initDateTimePicker, 100);
			return;
		}
		for (var i in langs) {
			if (!$.fn.datetimepicker.dates[i]) {
				setTimeout(initDateTimePicker, 100);
				return;
			}
		}

		$(dps).datetimepicker();
	}

	$(window).on('load', function () {
		var $dps = $(dps);
		if ($dps.size()) {
			var css = $dps.data('datetimepickerCss');
			if (css) {
				$.jcss(css);
			}
			var js = $dps.data('datetimepickerJs');
			if (js) {
				$.jscript(js, function() {
					$dps.each(function() {
						var i = $(this).data('language');
						var v = $(this).data('datetimepickerLangJs');
						if (i && v && !langs[i]) {
							$.jscript(v);
							langs[i] = v;
						}
					});
				});
			}
			initDateTimePicker();
		}
	});
})(jQuery);
if (typeof(panda) == "undefined") { panda = {}; }

(function($) {
	function ajaxFormSubmit() {
		var $f = $(this);
		var $a = $('#' + $f.attr('id') + '_alert').empty();

		$f.find('.has-error').removeClass('has-error').end().find('.p-field-errors').remove();
		if (!$f.isLoadMasked()) {
			$f.loadmask();
		}

		$.ajax({
			url: $f.data('ajaxAction'),
			method: 'post',
			data: $f.serializeArray(),
			dataType: 'json',
			success: function(d) {
				$a.palert('actionAlert', d, $f);
				if (d.result) {
					$f.vals(d.result);
					$f.find('div.p-datepicker, div.p-datetimepicker, div.p-timepicker').each(function() {
						var v = $(this).find('input').val();
						if (v) {
							$(this).datetimepicker('setValue', new Date(v));
						}
					});
				}
				if ($a.children().length) {
					$a.scrollIntoView();
				}
			},
			error: function(xhr, status, e) {
				$a.palert('ajaxJsonAlert', xhr, status, e, $f.data('ajaxServerError'));
			},
			complete: function() {
				$f.unloadmask();
			}
		});
		return false;
	}
			
	function ajaxSubmitHook($f) {
		// hook submit
		if (!$f.data("ajaxSubmitHooked") && $f.data('ajaxAction')) {
			$f.data('ajaxSubmitHooked', true).submit(ajaxFormSubmit);
		}
	}

	function ajaxLoadInnerForm($f) {
		var $c = $f.closest('.p-popup, .p-inner');
		var data = $f.serializeArray();
		if ($c.hasClass('p-inner')) {
			data.push({ name: '__inner', value: 'true' });
		}
		else {
			data.push({ name: '__popup', value: 'true' });
		}
		
		if ($f.attr('loadmask') != 'false') {
			$c.parent().loadmask();
		}

		$.ajax({
			url: $f.attr('action'),
			data: data,
			dataType: 'html',
			success: function(html, ts, xhr) {
				$c.parent().html(html);
			},
			error: function(xhr, ts, err) {
				$c.parent().html(xhr.responseText);
			},
			complete: function(xhr, ts) {
				$c.parent().unloadmask();
			}
		});
	}

	function actionHook($f) {
		// hook action
		if (!$f.data("actionHooked")) {
			$f.data('actionHooked', true)
				.find('input[data-action], button[data-action]').click(function() {
					$(this).closest('form').attr('action', $(this).data('action'));
				});
		}
	}

	function isSelfForm($f) {
		var t = $f.attr('target') || '_self';
		return t == '_self' || t == '_top' || t == '_parent';
	}

	function innerHook($f) {
		// hook inner, popup
		if ($f.data("hooked")) {
			return;
		}
		var $c = $f.closest('.p-popup, .p-inner');
		if ($c.length > 0) {
			$f.data('hooked', true);
			$f.submit(function(e) {
				e.preventDefault();
				ajaxLoadInnerForm($(this));
				return false;
			});
		}
	}

	function loadmaskHook($f) {
		// hook loadmask
		if ($f.data("hooked")) {
			return;
		}

		if ($f.height() > 20 && $f.attr('loadmask') != 'false') {
			$f.data('hooked', true);
			$f.submit(function() {
				$(this).loadmask();
			})
		}
	}

	$(window).on('load', function() {
		$('form').each(function() {
			var $f = $(this);

			// hook action
			actionHook($f);

			if (isSelfForm($f)) {
				// hook inner, popup
				innerHook($f);

				// hook loadmask
				if (panda.enable_loadmask_form) {
					loadmaskHook($f);
				}
				
				ajaxSubmitHook($f);
			}
		});
	});
})(jQuery);
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
				$.jscript(js, function() {
					$sns.each(function() {
						var i = $(this).data('summernoteLang');
						var v = $(this).data('summernoteLangJs');
						if (i && v && !langs[i]) {
							$.jscript(v);
							langs[i] = v;
						}
					});
				});
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
if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.loading = function(timeout) {
	$('body').loadmask({ mask: false, fixed: true, timeout: timeout || 1000 });
};

panda.page_sort = function(name, dir) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 's.c': name, 's.d': dir });
	return false;
};
panda.page_sort_reverse = function(name, dir) {
	return panda.page_sort(name, dir.toLowerCase() == "asc" ? "desc" : "asc");
};
panda.page_goto = function(s) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.s': s });
	return false;
};
panda.page_limit = function(l) {
	panda.loading();
	location.href = $.addQueryParams(location.href, { 'p.l': l });
	return false;
};
function plv_options(id, options) {
	var lv = document.getElementById(id);
	for (var p in options) {
		lv[p] = options[p];
	}
}

function plv_getForm(id) {
	return document.getElementById(id + "_form");
}

function plv_getBForm(id) {
	return document.getElementById(id + "_bform");
}

function plv_submitBForm(id, an, ps, t) {
	$('#' + id).loadmask();
	var bf = plv_getBForm(id);
	if (an) {
		if (typeof(an) == 'string') {
			bf.action = an;
		}
		else if (!ps) {
			ps = an;
		}
	}
	if (ps) {
		for (n in ps) {
			$('<input type="hidden" name="' + n + '"/>').val(ps[n]).appendTo(bf);
		}
	}
	bf.target = t ? t : "";
	bf.submit();
	return false;
}

function plv_submitCheckedRows(id, an, ps, t) {
	if (plv_enableCheckedValues(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_submitCheckedKeys(id, an, ps, t) {
	if (plv_enableCheckedKeys(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_enableCheckedValues(id, ns) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
				if (ns) {
					$(this).closest("tr").find("input.p-lv-cv").each(function() {
						var n = _plv_getPropertyName(this.name);
						if (ns == n || ns.contains(n)) {
							this.disabled = false;
						}
					});
				}
				else {
					$(this).closest("tr").find("input.p-lv-cv").prop("disabled", false);
				}
			}
			else {
				$(this).closest("tr").find("input.p-lv-cv").prop("disabled", true);
			}
		});
	return count;
}

function plv_enableCheckedKeys(id) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
			}
			$(this).closest("tr").find("input.p-lv-ck").prop("disabled", !this.checked);
		});
	return count;
}

function plv_getRowData(tr) {
	var d = {};
	$(tr).find("input.p-lv-cv").each(function () {
		var n = _plv_getPropertyName(this.name);
		d[n] = this.value;
	});
	return d;
}

function _plv_getPropertyName(n) {
	var i = n.lastIndexOf('.');
	if (i >= 0) {
		n = n.substring(i + 1);
	}
	return n;
}

function plv_getTBodyRows(id) {
	return $("#" + id + " .p-lv-tbody > tr");
}

function plv_checkAll(id, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var $lv = $("#" + id);

	_plv_setCheckAll($lv, check, true);
}

function plv_checkRow(id, row, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var trs = $("#" + id + " .p-lv-tbody > tr");
	_plv_selectRow(trs.eq(row), check);
}


function _plv_init_table($lv) {
	$lv.find(".p-lv-thead > tr > th").each(function() {
		var $th = $(this);
		if ($th.hasClass("p-lv-sortable")) {
			$th.click(function() {
				_plv_sort($lv.attr("id"), $(this).attr("column"));
			});
			if ($.browser.msie && $.browser.majorVersion < 7) {
				$th.mouseenter(function() {
					$(this).addClass("p-lv-hover");
					return false;
				}).mouseleave(function() {
					$(this).removeClass("p-lv-hover");
					return false;
				});
			}
		}
	});

	var icd = 0;
	var inc = 0;
	var $tb = $lv.find(".p-lv-tbody");
	$tb.click(_plv_onTBodyClick)
		.find("input.p-lv-cb")
			.each(function() {
				_plv_selectRow($(this).closest("tr"), this.checked);
				if (this.checked) {
					icd++;
				}
				else {
					inc++;
				}
			});
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$tb.mouseover(_plv_onTBodyMouseOver)
			.mouseout(_plv_onTBodyMouseOut);
	}
	
	$lv.find(".p-lv-thead input.p-lv-ca")
		.click(_plv_onAllCheck)
		.prop("checked", (icd > 0 && inc == 0));
}

function _plv_init($lv) {
	if ($lv.data("plistview")) {
		return;
	}
	$lv.data("plistview", true);
	_plv_init_table($lv);
}

function _plv_sort(id, cn) {
	var es = document.getElementById(id + "_sort");
	if (es) {
		$('#' + id).loadmask();

		var ed = document.getElementById(id + "_dir");
		if (cn == es.value) {
			ed.value = (ed.value.toLowerCase() == "asc" ? "desc" : "asc");
		}
		else {
			es.value = cn;
			ed.value = "asc";
		}
		document.getElementById(id + "_submit").click();
	}
}

function _plv_goto(id, p) {
	$('#' + id).loadmask();

	document.getElementById(id + "_form").reset();
	document.getElementById(id + "_start").value = p;
	var es = document.getElementById(id + "_sort");
	if (es && es.value == "") {
		es.disabled = true;
	}
	var ed = document.getElementById(id + "_dir");
	if (ed && ed.value == "") {
		ed.disabled = true;
	}
	document.getElementById(id + "_submit").click();
	return false;
}

function _plv_limit(id, n) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = n;
	document.getElementById(id + "_submit").click();
}

function _plv_selectRow(tr, c) {
	if (c) {
		tr.addClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
	else {
		tr.removeClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
}

function _plv_toggleRow($tr, ts) {
	var $lv = $tr.closest("div.p-lv");
	if ($lv.data('singleSelect')) {
		if ($tr.hasClass("p-lv-selected")) {
			if (ts || !$lv.data("untoggleSelect")) {
				_plv_selectRow($tr, false);
			}
		}
		else {
			_plv_selectRow($lv.find("tr.p-lv-selected"), false);
			_plv_selectRow($tr, true);
		}
	}
	else {
		if ($tr.hasClass("p-lv-selected")) {
			_plv_selectRow($tr, false);
		}
		else {
			_plv_selectRow($tr, true);
		}
	}

	var all = true;
	$lv.find(".p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (!this.checked) {
				all = false;
				return false;
			}
		});
	_plv_setCheckAll($lv, all);
}

function _plv_setCheckAll($lv, check, crows) {
	$lv.find(".p-lv-thead input.p-lv-ca").each(function() {
		this.checked = check;
		this.title = $(this).data(check ? 'selectNone' : 'selectAll');
	});
	$lv.find(".p-lv-cab").each(function() {
		var $b = $(this);
		$b.prop('checked', check)
			.find("i")
				.removeClass($b.data(check ? "iconSelectAll" : "iconSelectNone"))
				.addClass($b.data(check ? "iconSelectNone" : "iconSelectAll"))
				.get(0).nextSibling.data = ' ' + $b.data(check ? 'textSelectNone' : 'textSelectAll');
	});
	if (crows) {
		$lv.find(".p-lv-tbody > tr").each(function() {
			_plv_selectRow($(this), check);
		});
	}
}

function _plv_onAllCheck() {
	var c = this.checked;
	var $lv = $(this).closest(".p-lv");

	_plv_setCheckAll($lv, c, true);
}

function _plv_onAllClick(el) {
	var c = !($(el).prop('checked') || false);
	var $lv = $(el).closest(".p-lv");
	_plv_setCheckAll($lv, c, true);
}

function _plv_onTBodyClick(evt) {
	var $t = $(evt.target);
	
	if ($t.prop('tagName') == 'A') {
		return;
	}
	
	var $tr = $t.closest('tr');
	if ($t.hasClass('p-lv-cb')) {
		evt.stopPropagation();
		_plv_toggleRow($tr, true);
		return;
	}

	if ($tr.size() > 0) {
		_plv_toggleRow($tr);
		
		var $lv = $tr.closest("div.p-lv");
		var handler = $lv.get(0).onrowclick || $lv.data("onrowclick");
		panda_call(handler, $tr.get(0));
	}
}


function _plv_onTBodyMouseOver(evt) {
	$(evt.target).closest("tr.p-lv-tr").addClass("ui-state-hover p-lv-hover");
	return false;
}
function _plv_onTBodyMouseOut(evt) {
	$(evt.target).closest("tr.p-lv-tr").removeClass("ui-state-hover p-lv-hover");
	return false;
}

(function($) {
	$(window).on('load', function() {
		$('div.p-lv').each(function() {
			_plv_init($(this));
		});
	});
})(jQuery);

if (typeof(panda) == "undefined") { panda = {}; }

//------------------------------------------------------
panda.meta_props = function() {
	var m = {};
	$('meta').each(function() {
		var $t = $(this);
		var a = $t.attr('property');
		if (a && a.startsWith('s:')) {
			var v = $t.attr('content');
			m[a.substring(2)] = v;
		}
	});
	return m;
};

(function($) {
	$(window).on('load', function () {
		$('.navbar-toggle').click(function() {
			$(this).toggleClass('active');
		});
	});
})(jQuery);
(function($) {
	$(window).on('load', function() {
		// invoke onPageLoad function
		for (var i in window) {
			if (i.startsWith('onPageLoad') && typeof(window[i]) == 'function') {
				window[i]();
				window[i] = null;
			}
		}
	});
})(jQuery);
(function() {
	function _click(evt) {
		var $el = $(this);
		if ($el.parent().hasClass('disabled')) {
			evt.preventDefault();
		}
		else {
			var pn = $el.attr('pageno');
			if (pn >= 0) {
				var $pg = $el.closest('.p-pager');
				var js = $pg.data('click');
				if (js) {
					js = js.replace('$', pn);
					js = js.replace('#', (pn - 1) * $pg.data('limit'));
					if (eval(js) === false) {
						evt.preventDefault();
					}
				}
			}
		}
	}

	function _setActivePage($p, n) {
		var $u = $p.find('ul.pagination');
		$u.find('li.active').removeClass('active');

		var $n = $u.find('li.p-pager-page');

		var m = $p.data('pages');
		var b = n - Math.floor($n.size() / 2);
		if (b + $n.size() > m) b = m - $n.size() + 1;
		if (b < 1) b = 1;

		var s = $p.data('style');
		if (n > 1) {
			$u.find('.p-pager-first, .p-pager-prev').removeClass('hidden disabled');
			$u.find('.p-pager-prev>a').attr('pageno', n - 1);
		}
		else {
			$u.find('.p-pager-first').addClass(s.contains('F') ? 'disabled' : 'hidden');
			$u.find('.p-pager-prev').addClass(s.contains('P') ? 'disabled' : 'hidden');
		}

		$u.find('.p-pager-ellipsis-left')[b > 1 ? 'removeClass' : 'addClass']('hidden');
		$n.each(function() {
			var $a = $(this).find('a');
			$a.attr('pageno', b).text(b);
			if (b == n) {
				$(this).addClass('active');
			}
			b++;
		});
		$u.find('.p-pager-ellipsis-right')[b <= m ? 'removeClass' : 'addClass']('hidden');

		if (n < m) {
			$u.find('.p-pager-next, .p-pager-last').removeClass('hidden disabled');
			$u.find('.p-pager-next>a').attr('pageno', n + 1);
		}
		else {
			$u.find('.p-pager-next').addClass(s.contains('N') ? 'disabled' : 'hidden');
			$u.find('.p-pager-last').addClass(s.contains('L') ? 'disabled' : 'hidden');
		}
	}
	
	$.fn.ppager = function(api, pno) {
		if (api == 'getActivePage') {
			return this.find('ul.pagination>li.active>a').attr('pageno');
		}
		if (api == 'setActivePage') {
			return this.each(function() { _setActivePage($(this), pno); });
		}
		return this.each(function() {
			var $p = $(this);
			if ($p.attr("ppager") != "true") {
				$p.attr("ppager", "true");
				if ($p.data("click")) {
					$p.find("a[pageno]").click(_click);
				}
			}
		});
	};
	
	// PAGER DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-spy="ppager"]').ppager();
	});
})();
(function() {
	function panel_wheel(e, delta) {
		var o = this.scrollLeft;
		this.scrollLeft -= (delta * 40);
		if (o != this.scrollLeft) {
			e.preventDefault();
		}
	}

	// ==================
	$(window).on('load', function() {
		$('.p-panel-hscroll .panel-body').each(function() {
			var $t = $(this);
			if ($t.data('pwheelpanel')) {
				return;
			}
			$t.data('pwheelpanel', true).mousewheel(panel_wheel);
		});
	});
})();
(function($) {
	function _pqr_init($qr) {
		if ($qr.data("pqueryer")) {
			return;
		}
		$qr.data("pqueryer", true);
		_pqr_init_filters($qr);
	}

	function _pqr_init_filters($qr) {
		$qr.find(".p-qr-filters")
			.find('.form-group')
				.each(function() {
					if ($(this).hasClass('p-hidden')) {
						$(this).find("input,select,textarea").prop('disabled', true);
					}
				}).end()
			.find('.p-qr-remove').click(_pqr_onDelFilter).end()
			.find('.p-qr-clear').click(_pqr_onClearFilters).end()
			.find('.p-qr-select').change(_pqr_onAddFilter).end()
			.find('.p-qr-f-number-c, .p-qr-f-date-c, .p-qr-f-datetime-c, .p-qr-f-time-c')
				.on('change', _pqr_onBetweenChange)
				.end()
			.find('form')
				.submit(function() {
					$qr.loadmask();
				});
	}

	function _pqr_clearFieldValue() {
		switch (this.tagName) {
		case "INPUT":
			switch (this.type) {
			case "radio":
				if (this.name != 'm' && !this.name.endsWith(".m")) {
					this.checked = false;
				}
				break;
			case "checkbox":
				this.checked = false;
				break;
			case "text":
				this.value = "";
				break;
			}
			break;
		case "SELECT":
			if (!this.name.endsWith(".c")) {
				this.selectedIndex = 0;
			}
			break;
		case "TEXTAREA":
			this.value = "";
			break;
		}
	}

	function _pqr_onBetweenChange() {
		var $t = $(this);
		if ($t.val() == 'bt') {
			$t.nextAll().removeClass('p-hidden').find('INPUT').prop('disabled', false);
		}
		else {
			$t.nextAll().addClass('p-hidden').find('INPUT').prop('disabled', true);
		}
	}

	function _pqr_onAddFilter() {
		var e = this;
		if (e.selectedIndex > 0) {
			$(e).closest(".p-qr-filters")
				.find('.p-qr-fsi-' + e.value)
					.removeClass('p-hidden')
					.find("input,select,textarea").prop('disabled', false).end()
					.end()
				.fieldset('expand');
			e.options[e.selectedIndex].disabled = true;
			e.selectedIndex = 0;
		}
		return false;
	}

	function _pqr_onDelFilter() {
		var $g = $(this).closest(".form-group");
		$g.addClass('p-hidden')
			.find("input,select,textarea")
				.prop('disabled', true)
				.each(_pqr_clearFieldValue)
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find(".p-field-errors")
				.remove()
				.end()
			.closest(".p-qr-filters")
				.find('.p-qr-select > option[value=' + $g.data('item') + ']')
					.prop('disabled', false);
	}

	function _pqr_onClearFilters() {
		$(this).closest(".p-qr-form")
			.find(".p-field-errors")
				.remove()
				.end()
			.find(".p-label-error")
				.removeClass('p-label-error')
				.end()
			.find("input,select,textarea")
				.each(_pqr_clearFieldValue);
		return false;
	}

	$(window).on('load', function() {
		$('div.p-qr').each(function() {
			_pqr_init($(this));
		});
	});
})(jQuery);

(function($) {
	$(window).on('load', function() {
		var $t = $('.p-totop');
		if ($t.size() > 0) {
			$t.click(function() {
				$('html,body').animate({ scrollTop: 0 }, 'slow');
			});
	
			var $w = $(window);
			$w.scroll(function() {
				$t[$w.scrollTop() > $w.height() ? 'show' : 'hide']();
			});
		}
	});
})(jQuery);

(function($) {
	$.fn.ptrigger = function(option) {
		option = $.extend({ 'icon' : 'fa fa-remove' }, option);
		return this.each(function() {
			var $t = $(this);
			if ($t.data('ptriggerHooked')) {
				return;
			}
			$t.data('ptriggerHooked', true);
			var f = option.onclick || $t.data('ptrigger');
			if (!f || f == 'false') {
				return;
			}

			var i = option.icon || $t.data('ptrigger-icon');
			var $i = $('<i class="p-trigger ' + i + '"></i>');
			$t.addClass('p-has-trigger');
			$i.insertAfter($t)
			  .click(function() {
					if (f && f != "true" && f != true) {
						panda_call(f, $t.get(0));
					}
					else {
						if ($t.val() != '') {
							$t.val('').trigger('change');
						}
					}
				});
		});
	};
	
	// DATA-API
	// ==================
	$(window).on('load', function () {
		$('[data-ptrigger]').ptrigger();
	});
})(jQuery);
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
		var $ue = $u.children('.p-uploader-error');

		var $up = $('<div class="p-uploader-progress progress progress-striped" style="display: none"><div class="progress-bar progress-bar-info" style="width: 0%"></div></div>');
		$up.insertAfter($ub.length > 0 ? $ub : $uf);

		if ($ue.length < 1) {
			$ue = $('<div class="p-uploader-error"></div>').insertAfter($up);
		}
		
		if ($us.length < 1) {
			$us = $('<div class="p-uploader-sep"></div>').insertAfter($ue);
		}

		function _filesize(fs) {
			var sz = String.formatSize(fs);
			if (sz.length > 0) {
				sz = '(' + sz + ')';
			}
			return sz;
		}

		function _filename(fn) {
			var u = fn.lastIndexOf('/');
			var w = fn.lastIndexOf('\\');
			var i = u > w ? u : w;
			return fn.substr(i + 1);
		}
		
		function _info(fi) {
			var fid = fi.name, fnm = _filename(fi.name), fsz = fi.size, fct = fi.type;
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

		function _set_progress(v) {
			$up.children('.progress-bar').css({width: v + '%'});
		}
		
		function _remove() {
			$(this).closest('.p-uploader-item').fadeOut(function() { $(this).remove(); });
			$ue.empty();
		}

		function _start_upload() {
			loading = true;
			progress = 0;

			($ub.length ? $ub : $uf).addClass('p-hidden');
			$ue.empty();

			_set_progress(0);
			$up.show();
		}

		function _fake_progress() {
			_set_progress(progress++);
			if (progress < 90) {
				setTimeout(_fake_progress, 20);
			}
		}

		function _ajax_progress(e) {
			if (e.lengthComputable) {
				var p = Math.round(e.loaded * 100 / e.total);
				_set_progress(p);
			}
		}
		
		function _end_upload() {
			_set_progress(100);
			$up.hide();
			$uf.val("");
			($ub.length ? $ub : $uf).removeClass('p-hidden');
			loading = false;
		}

		function _on_success_uploaded(d) {
			var fn = $u.data('onUploaded');
			if (typeof(window[fn]) == 'function') {
				return window[fn](d);
			}
			return false;
		}
		
		function _upload_on_success(d) {
			_end_upload();

			$ue.palert('actionAlert', d);
			if (d.success) {
				if (_on_success_uploaded(d)) {
					return;
				}
				
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
		}

		function _upload_on_error(xhr, status, e) {
			_end_upload();
			$ue.palert('error', (e ? (e + "") : (xhr ? xhr.responseText : status)));
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
			_fake_progress();

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
				xhr: function() {
					var xhr = $.ajaxSettings.xhr();
					xhr.upload.addEventListener('progress', _ajax_progress);
					return xhr;
				},
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
(function($) {	
	$(window).on('load', function () {
		$('.p-viewfield').prev('input').change(function() {
			var v = $(this).val();
			$(this).next('.p-viewfield').text(v == '' ? '\u3000' : v);
		});
		
		$('.p-viewfield[data-format="html"]').each(function() {
			$('<a href="#" class="p-vf-code"><i class="fa fa-code"></i></a>').appendTo($(this).parent());
		});
		
		$('.p-vf-code').click(function() {
			var $t = $(this);
			var $p = $t.toggleClass('active').parent();
			var v = $p.find('input').val();
			$p.find('.p-viewfield').html($t.hasClass('active') ? v.escapeHtml() : v);
			return false;
		});
	});
})(jQuery);
