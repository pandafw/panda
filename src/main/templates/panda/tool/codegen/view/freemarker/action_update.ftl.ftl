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
			<li class="n-step-first n-step-self">${s}@p.text name="step-${ui.name}_input"/></li>
			<li class="n-step-arrow">${s}@p.text name="step-arrow-e">&gt;${s}/@p.text></li>
			<li class="n-step-next">${s}@p.text name="step-${ui.name}_confirm"/></li>
			<li class="n-step-arrow">${s}@p.text name="step-arrow-e">&gt;${s}/@p.text></li>
			<li class="n-step-last">${s}@p.text name="step-${ui.name}Success"/></li>
		</ul>
		<div class="n-clear"><hr/></div>
	${s}/#if>
</#if>

	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<#list ui.displayFieldList as f>
		<#if f.editTag?? && f.editTag.name?ends_with(".file")>
			<#assign _formenctype = "multipart/form-data" />
		</#if>
	</#list>
	${s}@p.form cssClass="n-eform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" method="post"<#if _formenctype?has_content> enctype="${_formenctype}"</#if><#if ui.theme?has_content> theme="${ui.theme}"</#if>>
		<#include "update-fields.ftl" />
		${s}#assign _buttons_ = [] />
<#if "true" == props['ui.input.confirm']!>
	${s}#if action.getTextAsBoolean('ui-input-confirm', false)>
		${s}#assign _buttons_ = _buttons_ + [{
			"icon": "icon-${ui.name}-confirm",
			"action": "${action.name}_${ui.name}_confirm",
			"text": "button-${ui.name}-confirm"
		}]/>
	${s}#else>
</#if>
		${s}#assign _buttons_ = _buttons_ + [{
			"icon": "icon-${ui.name}-execute",
			"action": "${action.name}_${ui.name}_execute",
			"text": "button-${ui.name}-execute"
		}]/>
<#if "true" == props['ui.input.confirm']!>
	${s}/#if>
</#if>
<#if "true" == props['ui.input.reset']!>
		${s}#assign _buttons_ = _buttons_ + [{
			"type": "reset",
			"icon": "icon-reset",
			"text": "button-reset"
		}]/>
</#if>
		<#include "input-star-actions.ftl"/>
	${s}/@p.form>

	<#include "edit-script.ftl" />
</div>

<@footer/>
