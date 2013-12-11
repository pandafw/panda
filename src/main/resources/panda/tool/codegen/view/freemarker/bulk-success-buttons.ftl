<#if ui.params.buttons?has_content>
	<#list ui.params.buttons?split(' ') as t>
		<#include "bulk-buttons-item.ftl"/>
	</#list>
</#if>
