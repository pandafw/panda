(function($) {
	"use strict";

	function _clearTimeout($el) {
		//if this element has delayed mask scheduled then remove it
		var t = $el.data("_mask_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_mask_timeout");
		}

		//if this element has unmask timeout scheduled then remove it
		t = $el.data("_unmask_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_unmask_timeout");
		}
	}

	function _stopEvent(evt) {
		evt.preventDefault();
		evt.stopPropagation();
	}

	function doMask($el, c) {
		if ($el.isLoadMasked()) {
			unMask($el);
		} else {
			_clearTimeout($el);
		}

		var $lm = $('<div class="ui-loadmask">');
		if (c.cssClass) {
			$lm.addClass(c.cssClass);
		}

		var $ll = $('<div class="ui-loadmask-load">');
		if (c.content) {
			$lm.append($(c.content));
		} else {
			var $li = $('<div class="ui-loadmask-icon">'),
				$lt = $('<div class="ui-loadmask-text">');

			$ll.append($li).append($lt);

			if (c.html || c.text) {
				$ll.addClass('ui-loadmask-hasmsg');
				if (c.html) {
					$lt.html(c.html);
				} else {
					$lt.text(c.text);
				}
			}
			$lm.append($ll);
		}

		if ($el.css("position") == "static") {
			$el.addClass("ui-loadmasked-relative");
		}
		if (c.mask) {
			$el.append($('<div class="ui-loadmask-mask"></div>'));
		}

		$el.append($lm).addClass("ui-loadmasked");

		if (c.timeout > 0) {
			$el.data("_unmask_timeout", setTimeout(function() {
				unMask($el);
			}, c.timeout));
		}
		if (c.keyboard) {
			$el.on('keydown.loadmask', _stopEvent);
		}
	}

	function unMask($el) {
		_clearTimeout($el);

		$el.off('.loadmask');
		$el.find(".ui-loadmask-mask, .ui-loadmask").remove();
		$el.removeClass("ui-loadmasked ui-loadmasked-relative");
	}

	$.loadmask = {
		defaults: {
			cssClass: '',		// css class for the mask element
			mask: true,			// add mask layer
			keyboard: true,		// add keydown event handler for the mask element to prevent input
			delay: 0,			// delay in milliseconds before element is masked. If unloadmask() is called before the delay times out, no mask is displayed. This can be used to prevent unnecessary mask display for quick processes.
			timeout: 0,			// timeout in milliseconds for automatically unloadmask
		}
	};

	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 * @param content  html content that will be add to the loadmask
	 * @param html  html message that will be display
	 * @param text  text message that will be display (html tag will be escaped)
	 */
	$.fn.loadmask = function(c) {
		if (typeof (c) == 'string') {
			c = { text: c };
		}
		c = $.extend({}, $.loadmask.defaults, c);
		return this.each(function() {
			var $el = $(this);
			if (c.delay > 0) {
				$el.data("_mask_timeout", setTimeout(function() {
					doMask($el, c);
				}, c.delay));
			}
			else {
				doMask($el, c);
			}
		});
	};

	/**
	 * Removes mask from the element(s). Accepts both single and multiple selectors.
	 */
	$.fn.unloadmask = function() {
		return this.each(function() {
			unMask($(this));
		});
	};

	/**
	 * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
	 */
	$.fn.isLoadMasked = function() {
		return this.hasClass("ui-loadmasked");
	};
})(jQuery);
