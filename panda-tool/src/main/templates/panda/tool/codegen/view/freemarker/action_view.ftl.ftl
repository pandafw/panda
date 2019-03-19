<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "edit-success-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

${s}#if r??>
	${s}@p.form cssClass="p-vform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" method="post"${gen.focusme(ui)}>
		<#include "edit-view-fields.ftl"/>
		<#include "edit-success-actions.ftl"/>
	${s}/@p.form>
${s}#else>
	<@sback/>
${s}/#if>
</div>

<@footinc step=""/>
<@footer/>
