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
		}] />
<#list ui.orderedColumnList as c>
${s}#if a.displayColumn("${c.name}")>
	${s}#assign _columns_ = _columns_ + [{
			"name": "${c.name}",
			"value": ${(c.value!true)?string},
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
				"${fp.name}": ${fp.value},
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
			"tooltip": text.getText("a.t.${c.name}-tip", "")
		}] />
${s}/#if>
</#list>


	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		headPager="true" singleSelect="true" toggleSelect="false" autosize="false"
		cssClass="p-lv-clickable" cssTable="table-hover table-striped"
		onrowclick="$.popup().callback(plv_getRowData(this));"
	/>
<#if ui.safeInclude??>
	${s}@safeinclude path="<#if ui.safeInclude?has_content>${ui.safeInclude}<#else>${action.simpleActionClass}_${ui.name}_popup-custom.ftl</#if>"/>
</#if>
</div>

<@footer/>
