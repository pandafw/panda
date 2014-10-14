<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<div class="p-header">
		<h3>${s}@p.text name="title-${d}{actionResult}">${s}@s.param>${s}@p.text name="title"/>${s}/@s.param>${s}/@p.text></h3>
	</div>

	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>

	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"nowrap": true,
			"fixed": true
		}, <#rt/>
<#list ui.orderedColumnList as c>
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
			"tooltip": action.getText("${actionDataFieldName}.${c.name}-tip", "")
		}<#if c_has_next>, </#if><#rt/>
</#list>

		] />

	${s}@p.listview id="${action.name}_${ui.name}" action="${action.name}_${ui.name}"
		list="${actionDataListFieldName}" columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		start="pg.s" limit="pg.l" total="pg.t" sort="so.c" dir="so.d" filters="qf" filterm="qm"
		headPager="true" singleSelect="true" toggleSelect="false" autosize="false"
		onrowclick="%{'$.popup().callback(nlv_getRowData(this));'}"
	/>
</div>

<@footer/>
