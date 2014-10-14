<#if ui.params.buttons?has_content>
	<#list ui.params.buttons?split(' ') as t><#if t?starts_with('*')>
		<#include "bulk-buttons-item.ftl"/>
	</#if></#list>
</#if>
