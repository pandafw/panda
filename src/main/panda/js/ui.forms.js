if (typeof(panda) == "undefined") { panda = {}; }

(function() {
	function preload() {
		$('body').append(
			'<div id="preload" class="p-vhidden">'
				+ '<div class="ui-loadmask"></div>'
				+ '<div class="p-loader-large-snake"></div>'
			+ '</div>');
	}
	
	function focus_form($w) {
		var $i = $w.find('form').eq(0);
		$i = $i.find('input,select,textarea,button');
		$i = $i.not(':hidden,:disabled,[readonly]').eq(0);
		if ($i.length > 0) {
			$i.focus();
			$('body').scrollTop(0).scrollLeft(0);
		}
	}
	
	function submit_form() {
		var form = this, 
			$f = $(this), 
			$b = $(this).closest('.popup, .inner'),
			lm = ($f.height() > 20 && $f.attr('loadmask') != 'false');
		if ($b.length > 0) {
			setTimeout(function() {
				var data = $f.serializeArray();
				if ($b.hasClass('inner')) {
					data.push({ name: '__inner', value: 'true' });
				}
				else {
					data.push({ name: '__popup', value: 'true' });
				}
				if (lm) {
					$b.parent().loadmask();
				}
				$.get(form.action, data, function(html) {
					$b.parent().unloadmask().html(html);
				}, 'html');
			}, 10);
			return false;
		}
		else {
			if (lm) {
				$f.loadmask();
			}
			return true;
		}
	}
	
	function hook_forms($w) {
		$w.find('form[hooked!=true]').each(function() {
			var $t = $(this);
			$t.attr('hooked', 'true');
			if (this.target == '' || this.target == '_self'
				|| this.target == '_top' || this.target == '_parent') {
				$t.submit(submit_form);
			}
		});
	}

	function ie6_hack_forms($w) {
		if ($.browser.msie && $.browser.majorVersion < 7) {
			var onclick = function() {
				var t = this;
				$(this).closest('form').find('input[type=submit],button').each(function() {
					if (this != t) {
						this.disabled = true;
					}
				});
			};
			
			$w.find('form[hacked!=true]')
				.find('button[type=submit]').each(function() {
					if (!this.onclick) {
						$(this).click(onclick);
					}
				}).end()
				.attr('hacked', 'true');
		}
	}
})();
