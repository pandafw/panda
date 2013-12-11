<#if ui.params.toolbar?has_content>

	<ul class="p-toolbar">
	<#list ui.params.toolbar?split(' ') as t>
		<#include "toolbar-item.ftl"/>
	</#list>
	</ul>
</#if>
