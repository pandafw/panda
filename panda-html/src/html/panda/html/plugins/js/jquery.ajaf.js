(function($) {
	"use strict";

	var _cssHidden = {
		position: 'absolute',
		top: '-9999px',
		left: '-9999px'
	};

	var _xhrOK = (function() {
		var input = document.createElement('input'),
			xhr = new XMLHttpRequest();
		input.type = 'file';
		return ('multiple' in input)
			&& typeof (xhr.upload) != 'undefined'
			&& typeof (FileList) != 'undefined'
			&& typeof (File) != 'undefined';
	})();

	function addFiles(fs, fadd) {
		if (fs) {
			if (typeof (fs) == "string") {
				fs = $(fs);
			}

			if ($.isArray(fs)) {
				$.each(fs, function(i, f) {
					fadd(f);
				});
			} else {
				$.each(fs, function(n, f) {
					if ($.isArray(f)) {
						$.each(f, function(i, f) {
							fadd(f, n);
						});
					} else {
						fadd(f, n);
					}
				});
			}
		}
	}

	function addParams(ps, padd) {
		if (ps) {
			function _addParams(n, v) {
				if ($.isArray(v)) {
					$.each(v, function(i, v) {
						padd(n, v);
					});
				} else {
					padd(n, v);
				}
			}

			if ($.isArray(ps)) {
				$.each(ps, function(i, d) {
					_addParams(d.name, d.value);
				});
			} else {
				$.each(ps, function(n, v) {
					_addParams(n, v)
				});
			}
		}
	}

	// jquery ajax wrapper
	function ajax(s) {
		var data = new FormData();

		addParams(s.data, function(n, v) {
			data.append(n, v);
		});

		addFiles(s.file, function(f, n) {
			if (f instanceof FileList) {
				$.each(f, function(i, f) {
					data.append(n, f);
				});
				return;
			}

			if (f instanceof File) {
				data.append(n, f);
				return;
			}

			var $f = $(f);
			n = n || $f.attr('name');
			$.each($f.prop('files'), function(i, f) {
				data.append(n, f);
			});
		});

		s = $.extend({}, s, {
			cache: false,
			contentType: false,
			processData: false,
			data: data
		});
		delete s.file;

		var xhr = $.ajaxSettings.xhr();
		var ufp = s.uprogress, dfp = s.dprogress;
		if (ufp || dfp) {
			if (ufp) {
				xhr.upload.addEventListener('progress', function(e) {
					if (e.lengthComputable) {
						ufp(e.loaded, e.total);
					}
				});
				delete s.uprogress;
			}

			if (dfp) {
				xhr.addEventListener('progress', function(e) {
					if (e.lengthComputable) {
						dfp(e.loaded, e.total);
					}
				});
				delete s.dprogress;
			}
		}

		xhr.addEventListener('readystatechange', function(e) {
			switch (xhr.readyState) {
			case XMLHttpRequest.HEADERS_RECEIVED:
				var cd = xhr.getResponseHeader('Content-Disposition');
				if (cd) {
					xhr.responseType = 'arraybuffer';
					var cds = cd.split(';');
					$.each(cds, function(i, v) {
						var sp = v.indexOf('=');
						if (sp > 0) {
							let k = v.substring(0, sp).trim().toLowerCase();
							if (k == 'filename' || k == 'filename*') {
								var fn = v.substring(sp+1).trim();
								if (fn.length > 1 && fn.charAt(0) == '"' && fn.charAt(fn.length-1) == '"') {
									fn = fn.substring(1, fn.length-1);
								}
								if (k == 'filename*') {
									var cp = fn.indexOf("''");
									if (sp >= 0) {
										fn = fn.substring(cp + 2);
									}
								}
								fn = decodeURIComponent(fn);
								if (!xhr.download || k == 'filename*') {
									xhr.download = fn;
								}
							}
						}
					});
					if (!xhr.download) {
						xhr.download = cd;
					}
				}
				break;
			case XMLHttpRequest.DONE:
				if (xhr.download) {
					var blob = new Blob([xhr.response]),
						url = window.URL.createObjectURL(blob),
						$a = $('<a>', { download: xhr.download, href: url }).css(_cssHidden);
					
					$('body').append($a);
					$a.get(0).click();
					setTimeout(function() {
						window.URL.revokeObjectURL(url);
						$a.remove();
					}, 200);
				}
				break;
			}
		});
		s.xhr = function() {
			return xhr;
		};

		return $.ajax(s);
	}

	function createIFrame(s) {
		var id = "ajaf_if_" + s.id;
		return $('<iframe>', { id: id, name: id, src: s.secureUrl }).css(_cssHidden).appendTo('body');
	}

	function createForm(s) {
		var id = 'ajaf_form_' + s.id;

		var $form = $('<form>', {
			id: id,
			name: id,
			action: s.url,
			method: s.method,
			target: 'ajaf_if_' + s.id
		}).css(_cssHidden).appendTo('body');

		addParams(s.data, function(n, v) {
			$('<input type="hidden">')
				.attr('name', n)
				.val(v)
				.appendTo($form);
		});

		$form.files = [];
		if (s.file) {
			$form.attr({
				method: 'POST',
				encoding: 'multipart/form-data',
				enctype: 'multipart/form-data'
			});

			addFiles(s.file, function(f, n) {
				var $f = $(f), $c = $f.clone().insertAfter($f);

				n = n || $f.attr('name');
				$f.attr({
					id: '',
					name: n
				}).appendTo($form);

				$form.files.push({ real: $f, copy: $c });
			});
		}

		return $form;
	}

	function httpData(xhr, type) {
		var data = type == "xml" ? xhr.responseXML : xhr.responseText;

		switch (type) {
		case "script":
			// If the type is "script", eval it in global context
			$.globalEval(data);
			break;
		case "json":
			// Get the JavaScript object, if JSON is used.
			data = $.parseJSON(data);
			break;
		case "html":
			// evaluate scripts within html
			$("<div>").html(data).evalScripts();
			break;
		}

		return data;
	}

	function ajaf(s) {
		s = $.extend({
			method: 'POST',
			forceAjaf: false,
			forceAjax: false
		}, s);

		if (s.forceAjax || ((_xhrOK) && !s.forceAjaf)) {
			return ajax(s);
		}

		s = $.extend({
			id: new Date().getTime(),
			secureUrl: 'javascript:false',
		}, s);

		var $if = createIFrame(s),
			$form = createForm(s),
			done = false, xhr = {};

		// Wait for a response to come back
		function callback(timeout) {
			if (done) {
				return;
			}
			done = true;

			var status = timeout == "timeout" ? "error" : "success";
			try {
				var ioe = $if.get(0);
				var doc = ioe.contentWindow.document || ioe.contentDocument || window.frames[ioe.id].document;
				if (doc && doc.body) {
					if (s.selector) {
						xhr.responseText = $(doc.body).find(s.selector).html();
					} else {
						var fc = doc.body.firstChild;
						var tn = (fc && fc.tagName) ? fc.tagName.toUpperCase() : "";
						if (tn == "TEXTAREA") {
							xhr.responseText = fc.value;
						} else if (tn == "PRE") {
							xhr.responseText = $(fc).text();
						} else {
							xhr.responseText = doc.body.innerHTML;
						}
					}
				}
				xhr.responseXML = (doc && doc.XMLDocument) ? doc.XMLDocument : doc;
			} catch (e) {
				status = "error";
				if (s.error) {
					s.error(xhr, status, e);
				}
			}

			// Recover real files
			$.each($form.files, function(i, f) {
				f.real.attr({
					id: f.copy.attr('id'),
					name: f.copy.attr('name')
				}).insertAfter(f.copy);
				f.copy.remove();
			});
			$form.remove();

			switch (status) {
			case "timeout":
				if (s.error) {
					s.error(xhr, status);
				}
				break;
			case "success":
				// Make sure that the request was successful or not modified
				try {
					// process the data (runs the xhr through httpData regardless of callback)
					var data = httpData(xhr, s.dataType);

					// If a local callback was specified, fire it and pass it the data
					if (s.success) {
						s.success(data, xhr);
					}
				} catch (e) {
					if (s.error) {
						s.error(xhr, status, e);
					}
				}
				break;
			}

			try {
				// The request was completed
				if (s.complete) {
					s.complete(xhr, status);
				}
			} finally {
				//clear up the created iframe after file uploaded.
				$if.unbind();
				setTimeout(function() {
					$if.remove();
				}, 100);
				xhr = null;
			}
		};

		// timeout checker
		if (s.timeout > 0) {
			setTimeout(function() {
				// Check to see if the request is still happening
				if (!done) {
					callback("timeout");
				}
			}, s.timeout);
		}

		// fake progress
		var fudp = s.uprogress || s.dprogress;
		if (fudp) {
			var loaded = 0;
			function _fake_progress() {
				fudp(loaded < 95 ? ++loaded : loaded, 100);
				if (!done) {
					setTimeout(_fake_progress, 10 + loaded);
				}
			}
			setTimeout(_fake_progress, 10);
		}

		if (s.beforeSend) {
			s.beforeSend(xhr, s);
		}

		// submit
		try {
			$form.submit();
		} catch (e) {
			if (s.error) {
				s.error(xhr, "send", e);
			}
		}

		$if.on('load', callback);
		return xhr;
	};

	$.ajaf = ajaf;

})(jQuery);

