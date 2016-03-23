<#include "common.ftl"/>
${s}#if actionErrors?has_content>
<@header/>

<div class="p-section">
	<@sheader/>
	<@swell/>

	${s}#include "/action-alert.ftl"/>
</div>

<@footer/>
${s}#else>
${s}@p.text var="_fn_" name="title-${ui.name}" escape="none">${s}@p.param name="title" value="#(title)"/>${s}/@p.text>
${s}@p.head expiry="0" charset="UTF-8" bom="true" filename="%{vars._fn_ + '.csv'}" attachment="true" contentType="text/comma-separated-values"/>
${s}#assign _columns_ = [] />
<#list ui.displayColumnList as c>
${s}#if a.displayField("${c.name}")>
	${s}#assign _columns_ = _columns_ + [{
			"name": "${c.name}",
			"header": a.getText("a.t.${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
				"type": "${c.format.type?replace('#', '\\x23')}"
			},
		</#if>
			"hidden": ${(c.hidden!false)?string}
		}] />
${s}/#if>
</#list>
${s}@p.csv list=result columns=_columns_/>
${s}/#if>