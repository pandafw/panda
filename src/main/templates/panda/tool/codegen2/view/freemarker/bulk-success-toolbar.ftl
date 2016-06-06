<#if ui.params.toolbar?has_content>
	<#assign _tb = false/>
	<#list ui.params.toolbar?split(' ') as _t><#if !(_t?starts_with('-'))>
		<#if _t?starts_with('*')>
			<#assign t = _t?substring(1)/>
		<#else>
			<#assign t = _t/>
		</#if>
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
