<#include "common.ftl"/>
${s}#if action.hasErrors()>
<@header/>

<div class="p-section">
	<div class="p-header">
		<h3>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></h3>
	</div>

	${s}#include "/nuts/exts/struts2/views/action-prompt-all.ftl"/>
</div>

<@footer/>
${s}#else>
${s}@p.text var="_fn_" name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text>
${s}@p.head charset="UTF-8" bom="true" contentType="text/comma-separated-values"
	noCache="true" attachment="true" filename="%{#_fn_ + '.csv'}"/>
${s}#assign _columns_ = [<#rt/>
<#list ui.displayColumnList as c>
{
	"name": "${c.name}",
	"header": action.getText("${actionDataFieldName}.${c.name}"), 
<#if c.format??>
	"format": {
	<#list c.format.paramList as fp>
		"${fp.name}": "${fp.value?replace('#', '\\x23')}",
	</#list>
		"type": "${c.format.type?replace('#', '\\x23')}"
	},
</#if>
	"hidden": ${(c.hidden!false)?string}
}<#if c_has_next>, </#if><#rt/>
</#list>
] />
${s}@p.csv list="${actionDataListFieldName}" columns=_columns_/>
${s}/#if>