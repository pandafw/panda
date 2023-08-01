<#if ui.params.toolbar?has_content>
	<#assign _tb = false/>
	<#list ui.params.toolbar?split(' ') as _t><#if _t?starts_with('*') || _t?starts_with('-')>
		<#assign t = _t?substring(1)/>
	<#if !_tb>

	<div class="ui-toolbar text-right"><ul>
		<#assign _tb = true/>
	</#if>
		<#include "edit-toolbar-item.ftl"/>
	</#if></#list>
	<#if _tb>
	</ul></div>
	</#if>
</#if>
