<#include "common.ftl"/>
<@header/>
<@headinc step="_execute"/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-success" ]/>
	<@swell step="-success"/>

	<#include "edit-success-toolbar.ftl"/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>

${s}#if r??>
	${s}@p.form cssClass="p-sform" id="${formId}" method="post"${gen.focusme(ui)}>
		<#include "edit-view-fields.ftl"/>
		<#include "edit-success-actions.ftl"/>
	${s}/@p.form>
${s}/#if>
</div>

<@footinc step="_execute"/>
<@footer/>
