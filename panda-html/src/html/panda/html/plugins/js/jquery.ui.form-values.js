(function($) {
	"use strict";

	$.fn.changeValue = function(v) {
		return this.each(function() {
			var $t = $(this), o = $t.val();

			$t.val(v);
			if (o != v) {
				$t.trigger('change');
			}
		});
	};

	$.fn.formClear = function(trigger) {
		this.find('textarea, select')[trigger ? 'changeValue' : 'val']('');
		this.find('input').each(function() {
			var $i = $(this);
			switch ($i.attr('type')) {
			case 'hidden':
			case 'button':
			case 'submit':
			case 'reset':
				break;
			case 'checkbox':
			case 'radio':
				var oc = $i.prop('checked');
				$i.prop('checked', false);
				if (oc && trigger) {
					$i.trigger('change');
				}
				break;
			default:
				$i[trigger ? 'changeValue' : 'val']('');
			}
		});
		return this;
	};

	$.fn.formValues = function(vs, trigger) {
		if (vs) {
			for (var n in vs) {
				var v = vs[n];
				this.find(':input').filter(function() { return this.name == n; }).each(function() {
					var $i = $(this);
					switch ($i.attr('type')) {
					case 'file':
					case 'button':
					case 'submit':
					case 'reset':
						break;
					case 'checkbox':
						var va = $.isArray(v) ? v : [ v ];
						var oc = $i.prop('checked'), nc = $.inArray($i.val(), va) >= 0;
						$i.prop('checked', nc);
						if (trigger && nc != oc) {
							$i.trigger('change');
						}
						break;
					case 'radio':
						var oc = $i.prop('checked'), nc = ($i.val() == v);
						$i.prop('checked', nc);
						if (trigger && nc && !oc) {
							$i.trigger('change');
						}
						break;
					default:
						trigger ? $i.changeValue(v) : $i.val(v);
						break;
					}
				});
			}
			return this;
		}

		var m = {}, a = this.serializeArray();
		$.each(a, function(i, v) {
			var ov = m[v.name];
			if (ov === undefined) {
				m[v.name] = v.value;
				return;
			}
			if ($.isArray(ov)) {
				ov.push(v.value);
				return;
			}
			m[v.name] = [ov, v.value];
		});
		return m;
	};

})(jQuery);

