/*
 * This file is part of Pandaw.
 * Copyright(C) 2013 Pandaw Develop Team.
 *
 * Pandaw is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 *
 * Pandaw is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pandaw. If not, see <http://www.gnu.org/licenses/>.
 */
if (typeof(pw) == "undefined") { pw = {}; }

(function() {
	function _click(evt) {
		var $el = $(this);
		if (!$el.hasClass('n-p-disabled')) {
			var pn = $el.attr("pageno");
			if (pn) {
				var $pg = $el.closest("div.n-p");
				var cmd = $pg.attr("click").replace("#", (pn - 1) * $pg.attr("limit"));
				eval(cmd);
			}
		}
	}
	
	pw.pager = function(o) {
		var $p = $(o);
		if ($p.attr("pager") != "true") {
			$p.attr("pager", "true");
			var $pn = $p.find(".n-p-pageno, .n-p-prev, .n-p-next, .n-p-first, .n-p-last, .n-p-active");

			if ($p.attr("click")) {
				$pn.click(_click);
			}
		}
		
		this.$el = $p;
		
		this.has = function(n) {
			return this.$el.find("[pageno=" + n + "]").size() > 0;
		};
		
		this.val = function(n) {
			this.$el.find(".n-p-active")
				.removeClass("n-p-active")
				.addClass("n-p-pageno")
			 	.click(_click);
			this.$el.find(".n-p-pageno[pageno=" + n + "]")
				.unbind()
				.removeClass("n-p-pageno")
				.addClass("n-p-active");
			
			if (n <= 1) {
				this.$el.find(".n-p-prev")
					.children().attr("pageno", n - 1)
					.end().parent().hide();
			}
			else {
				this.$el.find(".n-p-prev")
					.children().attr("pageno", n - 1)
					.end().parent().show();
			}
			
			if (n >= this.$el.attr("total")) {
				this.$el.find(".n-p-next")
					.children().attr("pageno", n + 1)
					.end().parent().hide();
			}
			else {
				this.$el.find(".n-p-next")
					.children().attr("pageno", n + 1)
					.end().parent().show();
			}
			return this;
		};
		
		return this;
	};
})();
