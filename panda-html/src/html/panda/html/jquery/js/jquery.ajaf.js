(function($) {
	"use strict";

	var _xhrOK = (function() {
		var input = document.createElement('input'),
			xhr = new XMLHttpRequest();
		input.type = 'file';
		return ('multiple' in input)
			&& typeof(xhr.upload) != 'undefined'
			&& typeof(FileList) != 'undefined'
			&& typeof(File) != 'undefined';
	})();

	function addFiles(fs, fadd) {
		if (fs) {
			if (typeof(fs) == "string") {
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

		if (s.progress) {
			var fp = s.progress;
			s.xhr = function() {
				var xhr = $.ajaxSettings.xhr();
				xhr.upload.addEventListener('progress', function(e) {
					if (e.lengthComputable) {
						fp(e.loaded, e.total);
					}
				});
				return xhr;
			};
			delete s.progress;
		}

		return $.ajax(s);
	}
	
	function createIFrame(s) {
		var id = "ajaf_if_" + s.id;
		return $('<iframe id="' + id + '" name="' + id + '" src="' + s.secureUrl + '"></iframe>')
			.css({
				position: 'absolute',
				top: '-9999px',
				left: '-9999px'
			})
			.appendTo('body');
	}
	
	function createForm(s) {
		var id = 'ajaf_form_' + s.id;

		var $form = $('<form></form>', {
				id: id,
				name: id,
				action: s.url,
				method: s.method,
				target: 'ajaf_if_' + s.id
			})
			.css({
				position: 'absolute',
				top: '-9999px',
				left: '-9999px'
			})
			.appendTo('body');

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
				
				$form.files.push({ real: $f, copy: $c});
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

		var $if = createIFrame(s);
		var $form = createForm(s);
		
		// Watch for a new set of requests
		if (s.start) {
			s.start();
		}

		var done = false, loaded = 0, xhr = {};

		// Wait for a response to come back
		function callback(timeout) {
			if (done) {
				return;
			}
			done = true;

			var status = timeout == "timeout" ? "error" : "success";
			try {
				var ioe = $if.get(0);
				var	doc = ioe.contentWindow.document || ioe.contentDocument || window.frames[ioe.id].document;
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
				// console.debug("jquery.ajaf(" + s.url + "): " + (xhr.responseText || xhr.responseXML));
			} catch (e) {
				status = "error";
				if (s.error) {
					s.error(xhr, status, e);
				}
			}

			// Recover real files
			for (var i = 0; i < $form.files.length; i++) {
				var f = $form.files[i];
				f.real.attr({
					id: f.copy.attr('id'),
					name: f.copy.attr('name')
				}).insertAfter(f.copy);
				f.copy.remove();
			}
			$form.remove();	

			if (status == "timeout") {
				if (s.error) {
					s.error(xhr, status);
				}
			} else if (status == "success") {
				// Make sure that the request was successful or not modified
				try {
					// process the data (runs the xhr through httpData regardless of callback)
					var data = httpData(xhr, s.dataType);

					// If a local callback was specified, fire it and pass it the data
					if (s.success) {
						s.success(data, xhr);
					}
				} catch(e) {
					if (s.error) {
						s.error(xhr, status, e);
					}
				}
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
		
		if (s.send) {
			s.send(xhr, s);
		}

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
		if (s.progress) {
			loaded++;
			function _fake_progress() {
				s.progress(loaded < 95 ? ++loaded : loaded, 100);
				if (!done) {
					setTimeout(_fake_progress, 10 + loaded);
				}
			}
			setTimeout(_fake_progress, 10);
		}

		// submit
		try {
			$form.submit();
		} catch(e) {
			if (s.error) {
				s.error(xhr, "send", e);
			}
		}
		
		$if.on('load', callback);
		return xhr;
	};

	$.ajaf = ajaf;

})(jQuery);

