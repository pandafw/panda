if (typeof(pw) == "undefined") { pw = {}; }

pw.viewfield = function(o) {
	var api = {
		el: $(o),
		val: function(v) {
			if (typeof(v) == 'undefined') {
				return this.el.val();
			}
			else {
				this.el.val(v).next().text(v);
				return this;
			}
		}
	};
	
	return api;
};
