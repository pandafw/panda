<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<#include "edit-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>

	<#list ui.displayFieldList as f>
		<#if f.editTag?? && f.editTag.name?ends_with(".file")>
			<#assign _formenctype = "multipart/form-data" />
		</#if>
	</#list>
	${s}@p.form cssClass="p-eform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" initfocus="${ui.focus?c}" method="post"<#if _formenctype?has_content> enctype="${_formenctype}"</#if><#if ui.theme?has_content> showDescrip="true" theme="${ui.theme}"</#if>>
		<#include "edit-update-fields.ftl" />
		${s}#assign _buttons_ = [] />
<#if "true" == props['ui.input.confirm']!>
	${s}#if a.inputConfirm>
		${s}#assign _buttons_ = _buttons_ + [{
			"icon": "icon-${ui.name}-confirm",
			"action": "~/${ui.name}_confirm",
			"text": "button-${ui.name}-confirm"
		}]/>
	${s}#else>
</#if>
		${s}#assign _buttons_ = _buttons_ + [{
			"icon": "icon-${ui.name}-execute",
			"action": "~/${ui.name}_execute",
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
		<#include "edit-actions.ftl"/>
	${s}/@p.form>

	<#include "edit-script.ftl" />
	<@safeinc step=""/>
</div>

<@footer/>
