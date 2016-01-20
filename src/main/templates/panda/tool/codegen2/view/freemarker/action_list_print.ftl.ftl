<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader/>

	${s}#include "/action-alert.ftl"/>

	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": text.getText("listview-th-number", ""),
			"fixed": true
		}, <#rt/>
<#list ui.orderedColumnList as c>
{
			"name": "${c.name}",
			"value": false,
			"header": text.getText("a.t.${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
				"type": "${c.format.type?replace('#', '\\x23')}"
			},
		</#if>
		<#if c.display??>
			"display": ${c.display?string},
		</#if>
		<#if c.hidden??>
			"hidden": ${c.hidden?string},
		</#if>
		<#if c.group??>
			"group": ${c.group?string},
		</#if>
		<#if c.nowrap??>
			"nowrap": ${c.nowrap?string},
		</#if>
			"sortable": false,
		<#if c.width?has_content>
			"width": "${c.width}",
		</#if>
			"tooltip": text.getText("a.t.${c.name}-tip", "")
		}<#if c_has_next>, </#if><#rt/>
</#list>

	] />

	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-striped table-striped" autosize="false" script="false"
	/>
</div>

<@footer/>
