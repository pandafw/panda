(function($) {
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

		var $form = $('<form></form>')
			.attr({
				id: id,
				name: id,
				action: s.url,
				method: 'POST',
				target: 'ajaf_if_' + s.id
			})
			.css({
				position: 'absolute',
				top: '-9999px',
				left: '-9999px'
			})
			.appendTo('body');

		$form.files = [];
		
		function addFile($f, n) {
			var $c = $f.clone();
			$c.insertAfter($f);

			n = n || $f.attr('name');
			$f.attr({
				id: '',
				name: n
			})
			.appendTo($form);
			
			$form.files[$form.files.length] = { real: $f, clon: $c};
		}
		
		if (s.file) {
			$form.attr({
				encoding: 'multipart/form-data',
				enctype: 'multipart/form-data'
			});

			if (typeof(s.file) == "string") {
				addFile($(s.file));
			}
			else if ($.isArray(s.file)) {
				for (var i = 0; i < s.file.length; i++) {
					addFile($(s.file[i]));
				}
			}
			else {
				for (var n in s.file) {
					addFile($(s.file[n]), n);
				}
			}
		}
		
		function addParam(n, v) {
			$('<input type="hidden">')
				.attr('name', n)
				.val(v)
				.appendTo($form);
		}

		function addParams(n, v) {
			if ($.isArray(v)) {
				for (var i = 0; i < v.length; i++) {
					addParam(n, v[i]);
				}
			}
			else {
				addParam(n, v);
			}
		}

		if (s.data) {
			if ($.isArray(s.data)) {
				for (var i = 0; i < s.data.length; i++) {
					addParams(s.data[i].name, s.data[i].value);
				}
			}
			else {
				for (var n in s.data) {
					var v = s.data[n];
					if (v) {
						addParams(n, v)
					}
				}
			}
		}

		return $form;
	}

	function httpData(xhr, type) {
		var data = type == "xml" ? xhr.responseXML : xhr.responseText;
		
		if (type == "script") {
			// If the type is "script", eval it in global context
			$.globalEval(data);
		}
		else if (type == "json") {
			// Get the JavaScript object, if JSON is used.
			data = $.parseJSON(data);
		}
		else if (type == "html") {
			// evaluate scripts within html
			$("<div>").html(data).evalScripts();
		}
		
		return data;
	}

	$.ajaf = function(s) {
		// TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
		s = $.extend({
				id: new Date().getTime(),
				secureUrl: 'javascript:false'
			}, s);
		
		var $if = createIFrame(s);
		var $form = createForm(s);
		
		// Watch for a new set of requests
		if (s.start) {
			s.start();
		}

		var done = false, xhr = {};

		// Wait for a response to come back
		var callback = function(timeout) {
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
					}
					else {
						var fc = doc.body.firstChild;
						var tn = (fc && fc.tagName) ? fc.tagName.toUpperCase() : "";
						if (tn == "TEXTAREA") {
							xhr.responseText = fc.value;
						}
						else if (tn == "PRE") {
							xhr.responseText = $(fc).text();
						}
						else {
							xhr.responseText = doc.body.innerHTML;
						}
					}
				}
				xhr.responseXML = (doc && doc.XMLDocument) ? doc.XMLDocument : doc;

				if (typeof(console) != "undefined") {
					console.debug("jquery.ajaf(" + s.url + "): " + (xhr.responseText || xhr.responseXML));
				}
			}
			catch(e) {
				status = "error";
				if (s.error) {
					s.error(xhr, status, e);
				}
			}

			// Revert files
			for (var i = 0; i < $form.files.length; i++) {
				var f = $form.files[i];
				f.real.attr({
					id: f.clon.attr('id'),
					name: f.clon.attr('name')
				}).insertAfter(f.clon);
				f.clon.remove();
			}
			$form.remove();	

			if (status == "timeout") {
				if (s.error) {
					s.error(xhr, status);
				}
			}
			else if (status == "success") {
				// Make sure that the request was successful or not modified
				try {
					// process the data (runs the xhr through httpData regardless of callback)
					var data = httpData(xhr, s.dataType);

					// If a local callback was specified, fire it and pass it the data
					if (s.success) {
						s.success(data, xhr);
					}
				}
				catch(e) {
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
			}
			finally {
				//clear up the created iframe after file uploaded.
				$if.unbind();
				setTimeout(function() {
					try {
						$if.remove();
					}
					catch(e) {
						if (typeof(console) != "undefined") {
							console.debug("jquery.ajaf(" + s.url + "): failed to remove " + $if);
						}
					}
				}, 100);
				xhr = null;
			}
		};
		
		if (s.send) {
			s.send(xhr, s);
		}

		// Timeout checker
		if (s.timeout > 0) {
			setTimeout(function(){
				// Check to see if the request is still happening
				if (!done) {
					callback("timeout");
				}
			}, s.timeout);
		}
		
		try {
			$form.submit();
		}
		catch(e) {
			if (s.error) {
				s.error(xhr, "send", e);
			}
		}
		
		$if.load(callback);

		return { abort: function(){} };	
	};
})(jQuery);

