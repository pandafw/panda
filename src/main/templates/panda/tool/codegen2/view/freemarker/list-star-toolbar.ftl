<#if ui.params.toolbar?has_content>
	<#assign _tb = false/>
	<#list ui.params.toolbar?split(' ') as t><#if t?starts_with('*')>
	<#if !_tb>

	<div class="p-toolbar-wrap"><ul class="p-toolbar">
		<#assign _tb = true/>
	</#if>
		<#include "list-toolbar-item.ftl"/>
	</#if></#list>
	<#if _tb>
	</ul><div class="clearfix"></div></div>
	</#if>
</#if>
