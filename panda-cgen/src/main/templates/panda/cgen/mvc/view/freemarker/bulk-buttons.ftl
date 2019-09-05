<#if ui.params.buttons?has_content>
	<#list ui.params.buttons?split(' ') as _b>
		<#if _b?starts_with('*') || _b?starts_with('-')>

			<#assign t = _b?substring(1)/>
			<#include "bulk-buttons-item.ftl"/>
		</#if>
	</#list>
</#if>
