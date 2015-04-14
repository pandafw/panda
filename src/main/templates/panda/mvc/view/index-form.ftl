<@p.form id="index" method="get" theme="simple">
	<@p.hidden name="p.s"/>
	<@p.hidden name="p.l"/>
	<@p.hidden name="s.c"/>
	<@p.hidden name="s.d"/>
	<#if (param.k)?has_content><@p.hidden name="k"/></#if>
	<#if (param.c)?has_content><@p.hidden name="c"/></#if>
	<#if (param.t)?has_content><@p.hidden name="t"/></#if>
</@p.form>
