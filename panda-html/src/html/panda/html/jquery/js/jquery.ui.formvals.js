(function($) {
	$.fn.vals = function(vs) {
		if (vs) {
			for (var n in vs) {
				var v = vs[n];
				this.find('input[name="' + n + '"]').each(function() {
					var $t = $(this);
					switch ($t.attr('type')) {
					case 'button':
					case 'file':
					case 'submit':
						break;
					case 'checkbox':
					case 'radio':
						$t.prop('checked', $t.val() == v);
						break;
					default:
						$t.changeValue(v);
					}
				});
			}
			return this;
		}

		var m = {}, a = this.serializeArray();
		$.each(a, function(i, v) {
			m[v.name] = v.value;
		});
		return m;
	};

})(jQuery);

