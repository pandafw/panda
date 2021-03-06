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
		${s}@p.div cssClass="p-buttons">
			${s}@p.submit action="./${ui.name}${props['ui.action.seperator']!'.'}execute" icon="icon-${ui.name}-execute" label="#(btn-${ui.name}-execute)"/>
			${s}@p.submit action="./${ui.name}${props['ui.action.seperator']!'.'}input" icon="icon-back" label="#(btn-back)"/>
		${s}/@p.div>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
</div>

<@footinc step="_confirm"/>
<@footer/>
