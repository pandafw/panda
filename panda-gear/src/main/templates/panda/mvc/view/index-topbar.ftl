<div class="index-topbar">
<#if _index_sorter_!(true) != false>
	<#include "index-sorter.ftl"/>
</#if>
<#if _index_pager_!(true) != false>
	<#include "index-pager.ftl"/>
</#if>
</div>
