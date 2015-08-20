<#include "common.ftl"/>
${s}#if action.hasErrors()>
<@header/>

<div class="p-section">
	<@sheader/>

	${s}#include "/panda/mvc/view/action-alert.ftl"/>
</div>

<@footer/>
${s}#else>
${s}@p.text var="_fn_" name="title-${ui.name}">${s}@p.param name="title" value="#(title)"/>${s}/@p.text>
${s}@p.head expiry="0" charset="UTF-8" bom="true" filename="%{vars._fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
${s}#assign _columns_ = [<#rt/>
<#list ui.displayColumnList as c>
{
	"name": "${c.name}",
	"header": text.getText("a.t.${c.name}"),
<#if c.format??>
	"format": {
	<#list c.format.paramList as fp>
		"${fp.name}": ${fp.value},
	</#list>
		"type": "${c.format.type?replace('#', '\\x23')}"
	},
</#if>
	"hidden": ${(c.hidden!false)?string}
}<#if c_has_next>, </#if><#rt/>
</#list>
] />
${s}@p.csv list=result columns=_columns_/>
${s}/#if>