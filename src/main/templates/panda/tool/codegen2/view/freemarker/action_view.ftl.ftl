<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "edit-success-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-vform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "edit-view-fields.ftl"/>
		<#include "edit-success-actions.ftl"/>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
<#if ui.safeInclude??>
	${s}@safeinclude path="<#if ui.safeInclude?has_content>${ui.safeInclude}<#else>${action.simpleActionClass}_${ui.name}-custom.ftl</#if>"/>
</#if>
</div>

<@footer/>
