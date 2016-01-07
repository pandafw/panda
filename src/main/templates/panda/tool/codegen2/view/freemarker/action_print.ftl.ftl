<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-vform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "view-fields.ftl"/>
	${s}/@p.form>
${s}/#if>
</div>

<@footer/>
