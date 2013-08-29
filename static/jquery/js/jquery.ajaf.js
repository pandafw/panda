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
		
		if (s.data) {
			for (var n in s.data) {
				var v = s.data[n];
				if (v) {
					if ($.isArray(v)) {
						for (var i = 0; i < v.length; i++) {
							addParam(n, v[i]);
						}
					}
					else {
						addParam(n, v);
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

	function handleError(s, xhr, status, e) {
		if ($.handleError) {
			$.handleError.apply(window, arguments);
		}
		else if (s.error) {
			s.error(xhr, status, e);
		}
	}
	
	$.ajaf = function(s) {
		// TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
		s = $.extend({
			id: new Date().getTime(),
			secureUrl: 'javascript:false'
		}, $.ajaxSettings, s);
		
		var $if = createIFrame(s);
		var $form = createForm(s);
		
		// Watch for a new set of requests
		if (s.global && ! $.active++ ) {
			$.event.trigger("ajaxStart");
		}			 

		var done = false, xhr = {};

		if (s.global) {
			$.event.trigger("ajaxSend", [xhr, s]);
		}

		// Wait for a response to come back
		var callback = function(timeout) {			
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
				handleError(s, xhr, null, e);
			}

			if (xhr || timeout == "timeout") {				
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

				done = true;

				var status;
				try {
					status = timeout == "timeout" ? "error" : "success";

					// Make sure that the request was successful or notmodified
					if (status == "success") {
						// process the data (runs the xhr through httpData regardless of callback)
						var data = httpData(xhr, s.dataType);	   

						// If a local callback was specified, fire it and pass it the data
						if (s.success) {
							s.success(data, status);
						}
	
						// Fire the global callback
						if (s.global) {
							$.event.trigger("ajaxSuccess", [xhr, s]);
						}
					} 
					else {
						handleError(s, xhr, status);
					}
				}
				catch(e) {
					handleError(s, xhr, status, e);
				}

				try {
					// Process result
					if (s.complete) {
						s.complete(xhr, status);
					}
	
					// The request was completed
					if (s.global) {
						$.event.trigger("ajaxComplete", [xhr, s]);
					}
	
					// Handle the global AJAX counter
					if (s.global && ! --$.active) {
						$.event.trigger("ajaxStop");
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
							handleError(s, xhr, null, e);
						}									
					}, 100);
					xhr = null;
				}
			}
		};
		
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
			handleError(s, xhr, null, e);
		}
		
		$if.load(callback);

		return { abort: function(){} };	
	};
})(jQuery);

