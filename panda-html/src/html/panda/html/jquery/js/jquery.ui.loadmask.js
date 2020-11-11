(function($) {
	function clearMaskTimeout($el) {
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

		//if this element has center timeout scheduled then remove it
		t = $el.data("_maskc_timeout");
		if (t) {
			clearTimeout(t);
			$el.removeData("_maskc_timeout");
		}
	}

	function maskElement($el, c) {
		if ($el.isLoadMasked()) {
			unmaskElement($el);
		}
		else {
			clearMaskTimeout($el);
		}
		
		if ($el.css("position") == "static") {
			$el.addClass("ui-loadmasked-relative");
		}
		$el.addClass("ui-loadmasked");

		if (c.mask !== false) {
			var $m = $('<div class="ui-loadmask-mask"></div>');
			//auto height fix for IE
			if ($.browser && $.browser.msie) {
				$m.height($el.height() + parseInt($el.css("padding-top")) + parseInt($el.css("padding-bottom")));
				$m.width($el.width() + parseInt($el.css("padding-left")) + parseInt($el.css("padding-right")));
			}
			$el.append($m);
		}
		
		//fix for z-index bug with selects in IE6
		if ($.browser && $.browser.msie && parseInt($.browser.version, 10) < 7) {
			$el.find("select").addClass("ui-loadmasked-hidden");
		}
		
		var $mb = $('<div class="ui-loadmask" style="display:none;"></div>');
		if (c.cssClass) {
			$mb.addClass(c.cssClass);
		}
		if (c.content) {
			$mb.append($(c.content));
		}
		else {
			var $tb = $('<table class="ui-loadmask-tb"></table>'),
				$tr = $('<tr>'),
				$ti = $('<td class="ui-loadmask-icon">').append($('<i>').addClass(c.iconClass));
			$tr.append($ti);

			if (c.html || c.text) {
				$mb.addClass('ui-loadmask-hasmsg');
				var $tx = $('<td class="ui-loadmask-text">');
				if (c.html) {
					$tx.html(c.html);
				}
				else {
					$tx.text(c.text);
				}
				$tr.append($tx);
			}
			$mb.append($tb.append($tr));
		}
		$el.append($mb);

		if (c.fixed || typeof($.fn.center) != 'function') {
			$mb.addClass('ui-loadmask-fixed');
		}
		else {
			$mb.center();
			$el.data("_maskc_timeout", setInterval(function() {
				$mb.center();
			}, 250));
		}
		$mb.show();
		
		if (c.timeout > 0) {
			$el.data("_unmask_timeout", setTimeout(function() {
				unmaskElement($el);
			}, c.timeout));
		}
	}

	function unmaskElement($el) {
		clearMaskTimeout($el);

		$el.find(".ui-loadmask-mask, .ui-loadmask").remove();
		$el.removeClass("ui-loadmasked ui-loadmasked-relative");
		$el.find("select").removeClass("ui-loadmasked-hidden");
	}

	$.loadmask = {
		defaults: {
			iconClass: 'ui-loadmask-loading'
		}
	};

	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 * @param cssClass css class for the mask element
	 * @param iconClass css class for the icon (default: ui-loadmask-loading)
	 * @param content  html content that will be add to the loadmask
	 * @param html  html message that will be display
	 * @param text  text message that will be display (html tag will be escaped)
	 * @param mask  add mask layer (default: true)
	 * @param fixed fixed position (default: false)
	 * @param delay Delay in milliseconds before element is masked (optional). If unloadmask() is called 
	 *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
	 *              mask display for quick processes.
	 */
	$.fn.loadmask = function(c) {
		if (typeof(c) == 'string') {
			c = { text: c };
		}
		c = $.extend({}, $.loadmask.defaults, c);
		return this.each(function() {
			if (c.delay !== undefined && c.delay > 0) {
				var $el = $(this);
				$el.data("_mask_timeout", setTimeout(function() {
					maskElement($el, c);
				}, c.delay));
			}
			else {
				maskElement($(this), c);
			}
		});
	};
	
	/**
	 * Removes mask from the element(s). Accepts both single and multiple selectors.
	 */
	$.fn.unloadmask = function() {
		return this.each(function() {
			unmaskElement($(this));
		});
	};
	
	/**
	 * Checks if a single element is masked. Returns false if mask is delayed or not displayed. 
	 */
	$.fn.isLoadMasked = function() {
		return this.hasClass("ui-loadmasked");
	};
})(jQuery);
