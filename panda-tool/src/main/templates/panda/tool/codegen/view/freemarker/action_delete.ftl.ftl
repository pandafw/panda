<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-confirm" ]/>
	<@swell step="-confirm"/>

	<#include "edit-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-cform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" focusme="${ui.focus?c}" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "edit-view-fields.ftl"/>
		${s}#assign _buttons_ = [{
			"icon": "icon-${ui.name}-execute",
			"action": "~/${ui.name}_execute",
			"text": "btn-${ui.name}-execute"
		}]/>
		<#include "edit-actions.ftl"/>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
	<@safeinc step="_confirm"/>
</div>

<@footer/>
