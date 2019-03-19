if (typeof(panda) == "undefined") { panda = {}; }

panda.viewfield = function(o) {
	var api = {
		el: $(o),
		val: function(v, t) {
			if (typeof(v) == 'undefined') {
				return this.el.val();
			}

			var o = this.el.val();
			this.el.val(v).next('.p-viewfield').text(v == '' ? '\u3000' : v);
			if (t && o != v) {
				this.el.trigger('change');
			}
			return this;
		}
	};
	
	return api;
};

(function($) {	
	$(window).on('load', function () {
		$('.p-viewfield[data-format="html"]').each(function() {
			var $t = $(this);
			$('<a href="#" class="p-vf-code"><i class="fa fa-code"></i></a>').appendTo($(this).parent());
		});
		
		$('.p-vf-code').click(function() {
			var $t = $(this);
			$t.toggleClass('active');
			
			var $p = $t.parent();
			var v = $p.find('input').val();
			var $d = $p.find('.p-viewfield').html($t.hasClass('active') ? v.escapePhtml() : v);
			return false;
		});
	});
})(jQuery);
