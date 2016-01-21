<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-success" ]/>
	<#include "edit-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-sform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "view-fields.ftl"/>
		<#include "edit-actions.ftl"/>
	${s}/@p.form>
${s}/#if>
</div>

<@footer/>
