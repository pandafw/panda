<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<div class="p-header">
		<h3>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></h3>
	</div>
	<#include "success-toolbar.ftl"/>

	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>

	${s}@p.form cssClass="p-vform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" "method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "view-fields.ftl"/>
		<#include "input-success-actions.ftl"/>
	${s}/@p.form>
</div>

<@footer/>
