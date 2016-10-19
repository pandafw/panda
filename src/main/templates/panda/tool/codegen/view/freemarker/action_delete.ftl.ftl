<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<div class="p-header">
		<h3>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></h3>
	</div>
	<#include "star-toolbar.ftl"/>

<#if "true" == props["ui.input.step"]!"">
	${s}#if action.getTextAsBoolean('ui-input-step', false)>
		<ul class="n-step">
			<li class="n-step-first n-step-self">${s}@p.text name="step-${ui.name}_confirm"/></li>
			<li class="n-step-arrow">${s}@p.text name="step-arrow-e">&gt;${s}/@p.text></li>
			<li class="n-step-last">${s}@p.text name="step-${ui.name}Success"/></li>
		</ul>
		<div class="n-clear"><hr/></div>
	${s}/#if>
</#if>
	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>

	${s}@p.form cssClass="p-cform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" focusme="${ui.focus?c}" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "view-fields.ftl"/>
		${s}#assign _buttons_ = [{
			"icon": "icon-${ui.name}-execute",
			"action": "${action.name}_${ui.name}_execute",
			"text": "button-${ui.name}-execute"
		}]/>
		<#include "input-star-actions.ftl"/>
	${s}/@p.form>
</div>

<@footer/>
