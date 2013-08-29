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
