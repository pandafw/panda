<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-confirm" ]/>
	<#include "edit-star-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-cform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "edit-view-fields.ftl"/>
		${s}@p.div cssClass="p-buttons">
			${s}@p.submit icon="icon-${ui.name}-execute" action="~/${ui.name}_execute">${s}@p.text name="button-${ui.name}-execute"/>${s}/@p.submit>
			${s}@p.submit icon="icon-back" action="~/${ui.name}_input">${s}@p.text name="button-back"/>${s}/@p.submit>
		${s}/@p.div>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
<#if ui.safeInclude??>
	${s}@safeinclude path="<#if ui.safeInclude?has_content>${ui.safeInclude}<#else>${action.simpleActionClass}_${ui.name}_confirm-custom.ftl</#if>"/>
</#if>
</div>

<@footer/>
