(function($) {
	function maskElement($el, conf) {
		//if this element has delayed mask scheduled then remove it and display the new one
		if ($el.data("_mask_timeout") !== undefined) {
			clearTimeout($el.data("_mask_timeout"));
			$el.removeData("_mask_timeout");
		}

		if ($el.isLoadMasked()) {
			unmaskElement($el);
		}
		
		if ($el.css("position") == "static") {
			$el.addClass("ui-loadmasked-relative");
		}
		$el.addClass("ui-loadmasked");

		if (conf.mask !== false) {
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
		
		var $mb = $('<div class="ui-loadmask" style="display:none;"></div>');
		if (conf.cssClass) {
			$mb.addClass(conf.cssClass);
		}
		if (conf.label) {
			$mb.append('<table class="ui-loadmask-msg"><tr><td>' + conf.label + '</td></tr></table>');
		}
		$el.append($mb);
		
		//calculate center position
		var el = 0, et = 0;
		var ew, eh;
		var mbw = $mb.width() - parseInt($mb.css("padding-left")) - parseInt($mb.css("padding-right"));
		var mbh = $mb.height() - parseInt($mb.css("padding-top")) - parseInt($mb.css("padding-bottom"));
		if (conf.window) {
			$w = $(window);
			el = $w.scrollLeft();
			et = $w.scrollTop();
			ew = $w.width();
			eh = $w.height();
		}
		else {
			ew = $el.width();
			eh = $el.height();
		}

		$mb.css({
			top: Math.round(et + (eh - mbh) / 2) + "px",
			left: Math.round(el + (ew - mbw) / 2) + "px"
		});
		
		$mb.show();
	}
	
	function unmaskElement($el) {
		//if this element has delayed mask scheduled then remove it
		if ($el.data("_mask_timeout") !== undefined) {
			clearTimeout($el.data("_mask_timeout"));
			$el.removeData("_mask_timeout");
		}
		
		$el.find(".ui-loadmask-mask, .ui-loadmask").remove();
		$el.removeClass("ui-loadmasked");
		$el.removeClass("ui-loadmasked-relative");
		$el.find("select").removeClass("ui-loadmasked-hidden");
	}
 
	/**
	 * Displays loading mask over selected element(s). Accepts both single and multiple selectors.
	 *
	 * @param label Text message that will be displayed on top of the mask besides a spinner (optional). 
	 * 				If not provided only mask will be displayed without a label or a spinner.  	
	 * @param delay Delay in milliseconds before element is masked (optional). If unmask() is called 
	 *              before the delay times out, no mask is displayed. This can be used to prevent unnecessary 
	 *              mask display for quick processes.   	
	 */
	$.fn.loadmask = function(conf) {
		if (typeof(conf) == 'string') {
			conf = { label: conf };
		}
		else {
			conf = conf || {};
		}
		return this.each(function() {
			if (conf.delay !== undefined && conf.delay > 0) {
				var $el = $(this);
				$el.data("_mask_timeout", setTimeout(function() {
					maskElement($el, conf);
				}, conf.delay));
			}
			else {
				maskElement($(this), conf);
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
