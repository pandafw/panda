<#if ui.params.actions?has_content>
	<#assign _uas = ui.params.actions?split(' ')/>
	<#if _uas?has_content>
		<#list _uas as _ua><#if _ua?starts_with('*')>
			<#assign _a = _ua?substring(1)/>
			<#include "input-actions-item.ftl"/>
		</#if></#list>
			${s}#include "/panda/exts/struts2/views/form-buttons.ftl"/>
			${s}@form_buttons buttons=_buttons_/>
	</#if>
</#if>
