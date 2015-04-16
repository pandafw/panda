<@p.url var="_url">
<#if _index_sorter_!(true) != false>
	<@p.param name="s.c" value="%{p.s.c}"/>
	<@p.param name="s.d" value="%{p.s.d}"/>
</#if>
	<@p.param name="p.l" value="%{p.p.l}"/>
	<#if (params.k)?has_content><@p.param name="k" value="%{p.k}"/></#if>
	<#if (params.c)?has_content><@p.param name="c" value="%{p.c}"/></#if>
	<#if (params.t)?has_content><@p.param name="t" value="%{p.t}"/></#if>
</@p.url>
<#assign _index_pager_link_ = v._url + "&amp;p.s=!{start}"/>
