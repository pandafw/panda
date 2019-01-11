<#include "common.ftl"/>
<@header/>
<@headinc step="_confirm"/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-confirm" ]/>
	<@swell step="-confirm"/>

	<#include "edit-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-cform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>"${gen.focusme(ui)} method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "edit-view-fields.ftl"/>
		${s}#assign _buttons_ = [{
			"icon": "icon-${ui.name}-execute",
			"action": "./${ui.name}${props['ui.action.seperator']!'.'}execute",
			"text": "btn-${ui.name}-execute"
		}]/>
		<#include "edit-actions.ftl"/>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
</div>

<@footinc step="_confirm"/>
<@footer/>
