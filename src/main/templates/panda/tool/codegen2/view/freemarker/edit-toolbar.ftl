<#if ui.params.toolbar?has_content>

	<div class="p-toolbar-wrap"><ul class="p-toolbar">
	<#list ui.params.toolbar?split(' ') as t>
		<#include "edit-toolbar-item.ftl"/>
	</#list>
	</ul><div class="clearfix"></div></div>
</#if>
