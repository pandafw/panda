<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "edit-success-toolbar.ftl"/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>

${s}#if r??>
	${s}@p.form cssClass="p-vform" id="${formId}" method="post"${gen.focusme(ui)}>
		<#include "edit-view-fields.ftl"/>
		<#include "edit-success-actions.ftl"/>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
</div>

<@footinc step=""/>
<@footer/>
