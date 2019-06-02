<#if action.orders?has_content>
<div class="index-sorter">
	<div class="btn-group">
	<#list action.orders?keys as _sc>
		<#assign _sd = (action.orders[_sc])!'asc'/>
		<#if ((params.s.c)!'') == _sc>
		<button class="btn btn-primary" onclick="panda.page_sort_reverse('${_sc}', '${(params.s.d)!}'); return false;"
			><@p.text name="lbl-sort-${_sc}"/> <i class="fa fa-sort-${(params.s.d)!}"></i></button>
		<#else>
		<button class="btn btn-default" onclick="panda.page_sort('${_sc}', '${_sd}'); return false;"
			><@p.text name="lbl-sort-${_sc}"/> <i class="fa fa-sort-${(params.s.d)!}"></i></button>
		</#if>
	</#list>
	</div>
</div>
</#if>
