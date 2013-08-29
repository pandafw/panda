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

function nlv_options(id, options) {
	var lv = document.getElementById(id);
	for (var p in options) {
		lv[p] = options[p];
	}
}

function nlv_getForm(id) {
	return document.getElementById(id + "_form");
}

function nlv_getBForm(id) {
	return document.getElementById(id + "_bform");
}

function nlv_submitBForm(id, an, ps) {
	$('#' + id).loadmask();
	var bf = nlv_getBForm(id);
	if (an) {
		if (typeof(an) == 'string') {
			bf.action = an;
		}
		else if (!ps) {
			ps = an;
		}
	}
	if (ps) {
		for (n in ps) {
			$('<input type="hidden" name="' + n + '"/>').val(ps[n]).appendTo(bf);
		}
	}
	bf.submit();
	return false;
}

function nlv_submitCheckedRows(id, an, ps) {
	if (nlv_enableCheckedValues(id) > 0) {
		nlv_submitBForm(id, an, ps);
	}
	return false;
}

function nlv_submitCheckedKeys(id, an, ps) {
	if (nlv_enableCheckedKeys(id) > 0) {
		nlv_submitBForm(id, an, ps);
	}
	return false;
}

function nlv_enableCheckedValues(id, ns) {
	var count = 0;
	$("#" + id + " .n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
				if (ns) {
					$(this).closest("tr").find("input.n-lv-cv").each(function() {
						var n = _nlv_getPropertyName(this.name);
						if (ns == n || ns.contains(n)) {
							this.disabled = false;
						}
					});
				}
				else {
					$(this).closest("tr").find("input.n-lv-cv").prop("disabled", false);
				}
			}
			else {
				$(this).closest("tr").find("input.n-lv-cv").prop("disabled", true);
			}
		});
	return count;
}

function nlv_enableCheckedKeys(id) {
	var count = 0;
	$("#" + id + " .n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
			}
			$(this).closest("tr").find("input.n-lv-ck").prop("disabled", !this.checked);
		});
	return count;
}

function nlv_getRowData(tr) {
	var d = {};
	$(tr).find("input.n-lv-cv").each(function () {
		var n = _nlv_getPropertyName(this.name);
		d[n] = this.value;
	});
	return d;
}

function _nlv_getPropertyName(n) {
	var i = n.lastIndexOf('.');
	if (i >= 0) {
		n = n.substring(i + 1);
	}
	return n;
}

function nlv_getTBodyRows(id) {
	return $("#" + id + " .n-lv-tbody > tr");
}

function nlv_checkAll(id, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var $lv = $("#" + id);

	_nlv_setCheckAll($lv, check, true);
}

function nlv_checkRow(id, row, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var trs = $("#" + id + " .n-lv-tbody > tr");
	_nlv_selectRow(trs.eq(row), check);
}

function _nlv_init_filters(id, collapse) {
	$("#" + id).find(".n-lv-filters").fieldset({
		collapse: collapse
	}).find('.n-tr-input').each(function() {
		if ($(this).hasClass('n-hidden')) {
			$(this).find("input,select,textarea").prop('disabled', true);
		}
	}).end().find('form').submit(function() {
		$('#' + id).loadmask();
	});
}

function _nlv_init_table(id, cfg) {
	var $lv = $("#" + id);

	if (cfg.autosize && !($.browser.ios || $.browser.android)) {
		$lv.addClass("n-lv-autosize");
		var $lvb = $lv.children(".n-lv-body").autosize();

		var $sth = $lv.find(".n-lv-thead");
		var $cth = $sth.clone();
		
		$cth.find('tr').append('<th><div class="n-lv-cell-last"></div></th>');
		
		var $bht = $('<table class="n-table"></table>').css('visibility', 'hidden').append($cth);
		
		$sth.removeClass('n-lv-thead').addClass('n-lv-thead-shadow');
		$sth.parent().css('margin-top', -1 - $sth.outerHeight() + "px");
		
		$('<div class="n-lv-body-head n-table-wrapper"></div>')
			.append($bht)
			.insertBefore($lvb).autosize({ 
				overflow: 'hidden',
				callback: function() {
					var $cths = $cth.find('.n-lv-cell');
					var $sths = $sth.find('.n-lv-cell');
					$cths.each(function(i) {
						var $sc = $sths.eq(i);
						if (!$sc.parent().is(':hidden')) {
							var cw = $sc.width();
							var hw = $sc.parent().width();
							$(this).width(cw >= hw ? cw : hw + 1);
						}
					});
					$bht.css('visibility', 'visible');
				}
			});
		$lvb.scroll(function() {
			$bht.css('margin-left', -1 - $lvb.scrollLeft() + "px");
		});
	}

	$lv.find(".n-lv-thead > tr > th").each(function() {
		var $th = $(this);
		if ($th.hasClass("n-lv-sortable")) {
			$th.click(function() {
				_nlv_onThClick.call(this, id);
			  });
			if ($.browser.msie && $.browser.majorVersion < 7) {
				$th.mouseenter(_nlv_onThMouseEnter).mouseleave(_nlv_onThMouseLeave);
			}
		}
	});

	var icd = 0;
	var inc = 0;
	var $tb = $lv.find(".n-lv-tbody");
	$tb.click(_nlv_onTBodyClick)
		.find("input.n-lv-cb")
			.each(function() {
				_nlv_selectRow($(this).closest("tr"), this.checked);
				if (this.checked) {
					icd++;
				}
				else {
					inc++;
				}
			});
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$tb.mouseover(_nlv_onTBodyMouseOver)
			.mouseout(_nlv_onTBodyMouseOut);
	}
	
	$lv.find(".n-lv-thead input.n-lv-ca")
		.click(_nlv_onAllCheck)
		.prop("checked", (icd > 0 && inc == 0));
}

function _nlv_sort(id, cn) {
	$('#' + id).loadmask();

	var es = document.getElementById(id + "_sort");
	var ed = document.getElementById(id + "_dir");
	if (cn == es.value) {
		ed.value = (ed.value.toLowerCase() == "asc" ? "desc" : "asc");
	}
	else {
		es.value = cn;
		ed.value = "asc";
	}

	document.getElementById(id + "_submit").click();
}

function _nlv_goto(id, p) {
	$('#' + id).loadmask();

	document.getElementById(id + "_form").reset();
	document.getElementById(id + "_start").value = p;
	document.getElementById(id + "_submit").click();
}

function _nlv_limit(id, l) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = l;
	document.getElementById(id + "_submit").click();
}

function _nlv_clearFieldValue() {
	switch (this.tagName) {
	case "INPUT":
		switch (this.type) {
		case "radio":
		case "checkbox":
			this.checked = false;
			break;
		case "text":
			this.value = "";
			break;
		}
		break;
	case "SELECT":
		if (!this.name.endsWith(".c")) {
			this.selectedIndex = 0;
		}
		break;
	case "TEXTAREA":
		this.value = "";
		break;
	}
}

function _nlv_selectRow(tr, c) {
	if (c) {
		tr.addClass("n-selected n-lv-selected")
		  .find("input.n-lv-cb").prop("checked", c);
	}
	else {
		tr.removeClass("n-selected n-lv-selected")
		  .find("input.n-lv-cb").prop("checked", c);
	}
}

function _nlv_toggleRow($tr, ts) {
	var $lv = $tr.closest("div.n-lv");
	if ($lv.get(0).singleSelect || $lv.attr("singleSelect") == "true") {
		if ($tr.hasClass("n-lv-selected")) {
			if (ts || !($lv.get(0).toggleSelect === false || $lv.attr("toggleSelect") === "false")) {
				_nlv_selectRow($tr, false);
			}
		}
		else {
			_nlv_selectRow($lv.find("tr.n-lv-selected"), false);
			_nlv_selectRow($tr, true);
		}
	}
	else {
		if ($tr.hasClass("n-lv-selected")) {
			_nlv_selectRow($tr, false);
		}
		else {
			_nlv_selectRow($tr, true);
		}
	}

	var all = true;
	$lv.find(".n-lv-tbody input.n-lv-cb")
		.each(function() {
			if (!this.checked) {
				all = false;
				return false;
			}
		});
	_nlv_setCheckAll($lv, all);
}

function _nlv_onBetweenChange(el) {
	var $t = $(el),
		d = $t.val() != 'bt',
		v = d ? 'hidden' : 'visible';
	if (d) {
		$t.nextAll('INPUT').val('');
	}
	$t.nextAll().css('visibility', v);
}

function _nlv_onAddFilter(e) {
	if (e.selectedIndex > 0) {
		$(e).closest(".n-lv-filters")
			.find('.n-lv-fsi-' + e.value)
				.removeClass('n-hidden')
				.find("input,select,textarea").prop('disabled', false).end()
				.end()
			.fieldset('expand');
		e.options[e.selectedIndex].disabled = true;
		e.selectedIndex = 0;
	}
	return false;
}

function _nlv_onDelFilter(e, n) {
	$(e).closest(".n-lv-filters")
		.find('.n-lv-fsi-' + n)
			.addClass('n-hidden')
			.find("input,select,textarea")
				.prop('disabled', true)
				.each(_nlv_clearFieldValue)
				.end()
			.find(".n-label-error")
				.removeClass('n-label-error')
				.end()
			.find(".n-field-errors")
				.remove()
				.end()
			.end()
		.find('.n-lv-fs-select>option[value=' + n + ']')
			.prop('disabled', false);
}

function _nlv_onClearFilters(e) {
	$(e).closest(".n-lv-filters-t")
		.find(".n-field-errors")
			.remove()
			.end()
		.find(".n-label-error")
			.removeClass('n-label-error')
			.end()
		.find("input,select,textarea")
			.each(_nlv_clearFieldValue);
	return false;
}

function _nlv_onThClick(id) {
	var cn = $(this).attr("column");
	_nlv_sort(id, cn);
}

function _nlv_setCheckAll($lv, check, crows) {
	$lv.find(".n-lv-thead input.n-lv-ca").each(function() {
		this.checked = check;
		this.title = $(this).attr(check ? 'selectNone' : 'selectAll');
	});
	$lv.find(".n-lv-cab").each(function() {
		var $b = $(this);
		$b.prop('checked', check)
		  .find(".n-button-icon")
		  	.removeClass($b.attr(check ? "iconSelectAll" : "iconSelectNone"))
		  	.addClass($b.attr(check ? "iconSelectNone" : "iconSelectAll"))
		  .end()
		  .find(".n-button-text")
		  	.text($b.attr(check ? 'textSelectNone' : 'textSelectAll'));
	});
	if (crows) {
		$lv.find(".n-lv-tbody > tr").each(function() {
			_nlv_selectRow($(this), check);
		});
	}
}

function _nlv_onAllCheck() {
	var c = this.checked;
	var $lv = $(this).closest(".n-lv");

	_nlv_setCheckAll($lv, c, true);
}

function _nlv_onAllClick(el) {
	var c = !($(el).prop('checked') || false);
	var $lv = $(el).closest(".n-lv");
	_nlv_setCheckAll($lv, c, true);
}

function _nlv_onTBodyClick(evt) {
	var $t = $(evt.target);
	
	var $tr = $t.closest('tr');
	if ($t.hasClass('n-lv-cb')) {
		evt.stopPropagation();
		_nlv_toggleRow($tr, true);
		return;
	}

	if ($tr.size() > 0) {
		_nlv_toggleRow($tr);
		
		var $lv = $tr.closest("div.n-lv");
		var handler = $lv.get(0).onrowclick || $lv.attr("onrowclick");
		switch (typeof(handler)) {
		case "function":
			handler.call($tr.get(0));
			break;
		case "string":
			handler = new Function(handler);
			handler.call($tr.get(0));
			break;
		}
	}
}

function _nlv_onThMouseEnter() {
	$(this).addClass("ui-state-hover n-lv-hover");
	return false;
}
function _nlv_onThMouseLeave() {
	$(this).removeClass("ui-state-hover n-lv-hover");
	return false;
}

function _nlv_onTBodyMouseOver(evt) {
	$(evt.target).closest("tr.n-lv-tr").addClass("ui-state-hover n-lv-hover");
	return false;
}
function _nlv_onTBodyMouseOut(evt) {
	$(evt.target).closest("tr.n-lv-tr").removeClass("ui-state-hover n-lv-hover");
	return false;
}
