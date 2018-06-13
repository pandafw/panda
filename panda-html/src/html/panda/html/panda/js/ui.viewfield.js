if (typeof(panda) == "undefined") { panda = {}; }

panda.viewfield = function(o) {
	var api = {
		el: $(o),
		val: function(v) {
			if (typeof(v) == 'undefined') {
				return this.el.val();
			}
			else {
				this.el.val(v).next().text(v == '' ? '\u3000' : v);
				return this;
			}
		}
	};
	
	return api;
};
