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
			if ($.browser.msie) {
				$m.height($el.height() + parseInt($el.css("padding-top")) + parseInt($el.css("padding-bottom")));
				$m.width($el.width() + parseInt($el.css("padding-left")) + parseInt($el.css("padding-right")));
			}
			$el.append($m);
		}
		
		//fix for z-index bug with selects in IE6
		if (parseInt($.browser.version,10) < 7) {
			$el.find("select").addClass("ui-loadmasked-hidden");
		}
		
		var $mb = $('<div class="ui-loadmask" style="display:none;"><div class="ui-loadmask-img"></div></div>');
		if (c.cssClass) {
			$mb.addClass(c.cssClass);
		}
		if (c.html) {
			$mb.html(html);
		}
		else if (c.label) {
			$mb.addClass('ui-loadmask-hasmsg').append('<table class="ui-loadmask-msg"><tr><td>' + c.label + '</td></tr></table>');
		}
		$el.append($mb);

		if (c.window) {
			$mb.addClass('ui-loadmask-fixed');
		}
		else {
			$mb.center();
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
 
	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 * @param cssClass css class for the mask element
	 * @param html  html content for the mask body
	 * @param label text message that will be display
	 * @param delay Delay in milliseconds before element is masked (optional). If unloadmask() is called 
	 *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
	 *              mask display for quick processes.
	 */
	$.fn.loadmask = function(c) {
		if (typeof(c) == 'string') {
			c = { label: c };
		}
		else {
			c = c || {};
		}
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
