<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-success" ]/>
	<@swell step="-success"/>

	<#include "edit-success-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-sform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>"${gen.focusme(ui)} method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "edit-view-fields.ftl"/>
		<#include "edit-success-actions.ftl"/>
	${s}/@p.form>
${s}/#if>
	<@safeinc step="_execute"/>
</div>

<@footer/>
