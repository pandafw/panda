<#if ui.params.toolbar?has_content>

	<div class="ui-toolbar text-right"><ul>
	<#list ui.params.toolbar?split(' ') as t>
		<#include "list-toolbar-item.ftl"/>
	</#list>
	</ul></div>
</#if>
