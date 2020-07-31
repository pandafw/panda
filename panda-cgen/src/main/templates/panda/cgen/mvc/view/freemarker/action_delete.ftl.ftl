<#include "common.ftl"/>
<@header/>
<@headinc step="_confirm"/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-confirm" ]/>
	<@swell step="-confirm"/>

	<#include "edit-toolbar.ftl"/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>

${s}#if r??>
	${s}@p.form cssClass="p-cform" id="${formId}" method="post"${gen.focusme(ui)}>
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
