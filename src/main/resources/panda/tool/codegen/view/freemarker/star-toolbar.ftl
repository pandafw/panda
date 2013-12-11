<#if ui.params.toolbar?has_content>
	<#assign _tb = false/>
	<#list ui.params.toolbar?split(' ') as t><#if t?starts_with('*')>
	<#if !_tb>

	<ul class="p-toolbar">
		<#assign _tb = true/>
	</#if>
		<#include "toolbar-item.ftl"/>
	</#if></#list>
	<#if _tb>
	</ul>
	</#if>
</#if>
