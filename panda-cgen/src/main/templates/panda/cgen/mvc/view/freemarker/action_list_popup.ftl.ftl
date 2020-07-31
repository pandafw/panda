<#include "common.ftl"/>
<@header/>
<@headinc step="_popup"/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>

	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": a.getText("listview-th-number", ""),
			"fixed": true
		}, <#list ui.orderedColumnList as c>{
			"name": "${c.name}",
			"value": ${(c.value!true)?string},
			"header": a.getFieldLabel("${c.name}"),
			"display": a.displayField("${c.name}"),
		<#if c.format??>
			"format": {
			<#list c.format.paramList as fp>
				"${fp.name}": ${fp.value},
			</#list>
			<#if c.format.type?has_content>
				"type": "${c.format.type?replace('#', '\\x23')}"<#if c.format.pattern?has_content>,</#if>
			</#if>
			<#if c.format.pattern?has_content>
				"pattern": "${c.format.pattern?replace('#', '\\x23')}"
			</#if>
			},
		</#if>
		<#if c.filter?? && c.filter.type != "none" && c.filter.type != "">
			"filter": {
				"type": "${c.filter.type?replace('#', '\\x23')}",
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
				"enable": a.filterField("${c.name}")
			},
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
		<#if c.width?has_content>
			"width": "${c.width}",
		</#if>
			"tooltip": a.getFieldTooltip("${c.name}")
		}<#if c_has_next>, </#if></#list>] />


	${s}@p.listview id="${action.name}_${gen.trimUiName(ui.name)}" action="./${ui.name}" 
		list=result.list columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		headPager="true" singleSelect="true" untoggleSelect="true"
		cssClass="p-lv-clickable" cssTable="table-hover table-striped"
		onrowclick="$.popup().callback(plv_getRowData(this));"
	/>
</div>

<@footinc step="_popup"/>
<@footer/>
