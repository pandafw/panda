<@p.form id="index" method="get" theme="simple">
<#if _index_pager_!(true) != false>
	<@p.hidden name="p.s"/>
	<@p.hidden name="p.l"/>
</#if>
<#if _index_sorter_!(true) != false>
	<@p.hidden name="s.c"/>
	<@p.hidden name="s.d"/>
</#if>
	<#if (params.k)?has_content><@p.hidden name="k"/></#if>
	<#if (params.c)?has_content><@p.hidden name="c"/></#if>
	<#if (params.t)?has_content><@p.hidden name="t"/></#if>
</@p.form>
