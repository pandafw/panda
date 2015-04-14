<#if action.orders?has_content>
<div class="index-sorter">
	<div class="btn-group">
	<#list action.orders?keys as sc>
		<#assign sd = (action.orders[sc])!'asc'/>
		<#if ((params.s.c)!'') == sc>
		<button class="btn btn-primary" onclick="sl_sorta('index', '${sc}', '${(params.s.d)!}'); return false;"
			><@p.text name="label-sort-${sc}"/> <i class="fa fa-sort-${(params.s.d)!}"></i></button>
		<#else>
		<button class="btn btn-default" onclick="sl_sortn('index', '${sc}', '${sd}'); return false;"
			><@p.text name="label-sort-${sc}"/> <i class="fa fa-sort-${(params.s.d)!}"></i></button>
		</#if>
	</#list>
	</div>
</div>
</#if>
