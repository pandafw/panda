<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader/>

	${s}#include "/panda/mvc/view/action-alert.ftl"/>

	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"nowrap": true,
			"fixed": true
		}, <#rt/>
<#list ui.orderedColumnList as c>
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
		<#if c.filter??>
			"filter": {
			<#if c.filter.display??>
				"display": ${c.filter.display?string},
			</#if>
			<#if c.filter.label??>
				"label": "${c.filter.label}",
			</#if>
			<#if c.filter.tooltip??>
				"tooltip": "${c.filter.tooltip}",
			</#if>
			<#if c.filter.fixed??>
				"fixed": ${c.filter.fixed?string},
			</#if>
			<#list c.filter.paramList as fp>
				"${fp.name}": "${fp.value?replace('#', '\\x23')}",
			</#list>
				"type": "${c.filter.type?replace('#', '\\x23')}"
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
		<#if c.sortable??>
			"sortable": ${c.sortable?string},
		</#if>
		<#if c.filterable??>
			"filterable": ${c.filterable?string},
		</#if>
		<#if c.width?has_content>
			"width": "${c.width}",
		</#if>
			"tooltip": text.getText("a.t.${c.name}-tip", ""),
		}<#if c_has_next>, </#if><#rt/>
</#list>

		] />

	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		headPager="true" singleSelect="true" toggleSelect="false" autosize="false"
		onrowclick="%{'$.popup().callback(plv_getRowData(this));'}"
	/>
</div>

<@footer/>
