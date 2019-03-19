function plv_options(id, options) {
	var lv = document.getElementById(id);
	for (var p in options) {
		lv[p] = options[p];
	}
}

function plv_getForm(id) {
	return document.getElementById(id + "_form");
}

function plv_getBForm(id) {
	return document.getElementById(id + "_bform");
}

function plv_submitBForm(id, an, ps, t) {
	$('#' + id).loadmask();
	var bf = plv_getBForm(id);
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
	bf.target = t ? t : "";
	bf.submit();
	return false;
}

function plv_submitCheckedRows(id, an, ps, t) {
	if (plv_enableCheckedValues(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_submitCheckedKeys(id, an, ps, t) {
	if (plv_enableCheckedKeys(id) > 0) {
		plv_submitBForm(id, an, ps, t);
	}
	return false;
}

function plv_enableCheckedValues(id, ns) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
				if (ns) {
					$(this).closest("tr").find("input.p-lv-cv").each(function() {
						var n = _plv_getPropertyName(this.name);
						if (ns == n || ns.contains(n)) {
							this.disabled = false;
						}
					});
				}
				else {
					$(this).closest("tr").find("input.p-lv-cv").prop("disabled", false);
				}
			}
			else {
				$(this).closest("tr").find("input.p-lv-cv").prop("disabled", true);
			}
		});
	return count;
}

function plv_enableCheckedKeys(id) {
	var count = 0;
	$("#" + id + " .p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (this.checked) {
				count++;
			}
			$(this).closest("tr").find("input.p-lv-ck").prop("disabled", !this.checked);
		});
	return count;
}

function plv_getRowData(tr) {
	var d = {};
	$(tr).find("input.p-lv-cv").each(function () {
		var n = _plv_getPropertyName(this.name);
		d[n] = this.value;
	});
	return d;
}

function _plv_getPropertyName(n) {
	var i = n.lastIndexOf('.');
	if (i >= 0) {
		n = n.substring(i + 1);
	}
	return n;
}

function plv_getTBodyRows(id) {
	return $("#" + id + " .p-lv-tbody > tr");
}

function plv_checkAll(id, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var $lv = $("#" + id);

	_plv_setCheckAll($lv, check, true);
}

function plv_checkRow(id, row, check) {
	if (typeof(check) == 'undefined') {
		check = true;
	}

	var trs = $("#" + id + " .p-lv-tbody > tr");
	_plv_selectRow(trs.eq(row), check);
}


function _plv_init_table($lv) {
	if ($lv.data("autosize") == 'true') {
		$lv.addClass("p-lv-autosize");
		var $lvb = $lv.children(".p-lv-body").autosize();

//		var $sth = $lv.find(".p-lv-thead");
//		var $cth = $sth.clone();
//		
//		$cth.find('tr').append('<th><div class="p-lv-cell-last"></div></th>');
//		
//		var $bht = $('<table class="p-table"></table>').css('visibility', 'hidden').append($cth);
//		
//		$sth.removeClass('p-lv-thead').addClass('p-lv-thead-shadow');
//		$sth.parent().css('margin-top', -1 - $sth.outerHeight() + "px");
//		
//		$('<div class="p-lv-body-head p-table-wrapper"></div>')
//			.append($bht)
//			.insertBefore($lvb).autosize({ 
//				overflow: 'hidden',
//				callback: function() {
//					var $cths = $cth.find('.p-lv-cell');
//					var $sths = $sth.find('.p-lv-cell');
//					$cths.each(function(i) {
//						var $sc = $sths.eq(i);
//						if (!$sc.parent().is(':hidden')) {
//							var cw = $sc.width();
//							var hw = $sc.parent().width();
//							$(this).width(cw >= hw ? cw : hw + 1);
//						}
//					});
//					$bht.css('visibility', 'visible');
//				}
//			});
//		$lvb.scroll(function() {
//			$bht.css('margin-left', -1 - $lvb.scrollLeft() + "px");
//		});
	}

	$lv.find(".p-lv-thead > tr > th").each(function() {
		var $th = $(this);
		if ($th.hasClass("p-lv-sortable")) {
			$th.click(function() {
				_plv_sort($lv.attr("id"), $(this).attr("column"));
			});
			if ($.browser.msie && $.browser.majorVersion < 7) {
				$th.mouseenter(function() {
					$(this).addClass("p-lv-hover");
					return false;
				}).mouseleave(function() {
					$(this).removeClass("p-lv-hover");
					return false;
				});
			}
		}
	});

	var icd = 0;
	var inc = 0;
	var $tb = $lv.find(".p-lv-tbody");
	$tb.click(_plv_onTBodyClick)
		.find("input.p-lv-cb")
			.each(function() {
				_plv_selectRow($(this).closest("tr"), this.checked);
				if (this.checked) {
					icd++;
				}
				else {
					inc++;
				}
			});
	if ($.browser.msie && $.browser.majorVersion < 7) {
		$tb.mouseover(_plv_onTBodyMouseOver)
			.mouseout(_plv_onTBodyMouseOut);
	}
	
	$lv.find(".p-lv-thead input.p-lv-ca")
		.click(_plv_onAllCheck)
		.prop("checked", (icd > 0 && inc == 0));
}

function _plv_init($lv) {
	if ($lv.data("plistview")) {
		return;
	}
	$lv.data("plistview", true);
	_plv_init_table($lv);
}

function _plv_sort(id, cn) {
	var es = document.getElementById(id + "_sort");
	if (es) {
		$('#' + id).loadmask();

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
}

function _plv_goto(id, p) {
	$('#' + id).loadmask();

	document.getElementById(id + "_form").reset();
	document.getElementById(id + "_start").value = p;
	var es = document.getElementById(id + "_sort");
	if (es && es.value == "") {
		es.disabled = true;
	}
	var ed = document.getElementById(id + "_dir");
	if (ed && ed.value == "") {
		ed.disabled = true;
	}
	document.getElementById(id + "_submit").click();
	return false;
}

function _plv_limit(id, n) {
	$('#' + id).loadmask();

	document.getElementById(id + "_limit").value = n;
	document.getElementById(id + "_submit").click();
}

function _plv_selectRow(tr, c) {
	if (c) {
		tr.addClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
	else {
		tr.removeClass("p-selected p-lv-selected")
		  .find("input.p-lv-cb").prop("checked", c);
	}
}

function _plv_toggleRow($tr, ts) {
	var $lv = $tr.closest("div.p-lv");
	if ($lv.data('singleSelect')) {
		if ($tr.hasClass("p-lv-selected")) {
			if (ts || !$lv.data("untoggleSelect")) {
				_plv_selectRow($tr, false);
			}
		}
		else {
			_plv_selectRow($lv.find("tr.p-lv-selected"), false);
			_plv_selectRow($tr, true);
		}
	}
	else {
		if ($tr.hasClass("p-lv-selected")) {
			_plv_selectRow($tr, false);
		}
		else {
			_plv_selectRow($tr, true);
		}
	}

	var all = true;
	$lv.find(".p-lv-tbody input.p-lv-cb")
		.each(function() {
			if (!this.checked) {
				all = false;
				return false;
			}
		});
	_plv_setCheckAll($lv, all);
}

function _plv_setCheckAll($lv, check, crows) {
	$lv.find(".p-lv-thead input.p-lv-ca").each(function() {
		this.checked = check;
		this.title = $(this).data(check ? 'selectNone' : 'selectAll');
	});
	$lv.find(".p-lv-cab").each(function() {
		var $b = $(this);
		$b.prop('checked', check)
		  .find("i")
		  	.removeClass($b.data(check ? "iconSelectAll" : "iconSelectNone"))
		  	.addClass($b.data(check ? "iconSelectNone" : "iconSelectAll"))
		  	.get(0).nextSibling.data = ' ' + $b.data(check ? 'textSelectNone' : 'textSelectAll');
	});
	if (crows) {
		$lv.find(".p-lv-tbody > tr").each(function() {
			_plv_selectRow($(this), check);
		});
	}
}

function _plv_onAllCheck() {
	var c = this.checked;
	var $lv = $(this).closest(".p-lv");

	_plv_setCheckAll($lv, c, true);
}

function _plv_onAllClick(el) {
	var c = !($(el).prop('checked') || false);
	var $lv = $(el).closest(".p-lv");
	_plv_setCheckAll($lv, c, true);
}

function _plv_onTBodyClick(evt) {
	var $t = $(evt.target);
	
	if ($t.prop('tagName') == 'A') {
		return;
	}
	
	var $tr = $t.closest('tr');
	if ($t.hasClass('p-lv-cb')) {
		evt.stopPropagation();
		_plv_toggleRow($tr, true);
		return;
	}

	if ($tr.size() > 0) {
		_plv_toggleRow($tr);
		
		var $lv = $tr.closest("div.p-lv");
		var handler = $lv.get(0).onrowclick || $lv.data("onrowclick");
		panda_call(handler, $tr.get(0));
	}
}


function _plv_onTBodyMouseOver(evt) {
	$(evt.target).closest("tr.p-lv-tr").addClass("ui-state-hover p-lv-hover");
	return false;
}
function _plv_onTBodyMouseOut(evt) {
	$(evt.target).closest("tr.p-lv-tr").removeClass("ui-state-hover p-lv-hover");
	return false;
}

(function($) {
	$(window).on('load', function() {
		$('div.p-lv').each(function() {
			_plv_init($(this));
		});
	});
})(jQuery);

