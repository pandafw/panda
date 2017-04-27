<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-success" ]/>
	<@swell step="-success"/>

	<#include "bulk-success-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>
	<br/>

${s}#if result?has_content>
	${s}#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": a.getText("listview-th-rownum", ""),
			"fixed": true
		}, <#list ui.orderedColumnList as c>{
			"name": "${c.name}",
			"header": a.getFieldLabel("${c.name}"),
			"display": <#if c.display?has_content>${c.display?string}<#else>a.displayField("${c.name}")</#if>,
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
		<#if c.hidden??>
			"hidden": ${c.hidden?string},
		</#if>
		<#if c.group??>
			"group": ${c.group?string},
		</#if>
			"sortable": false,
			"tooltip": a.getFieldTooltip("${c.name}")
		}<#if c_has_next>, </#if></#list>] />


	${s}@p.listview id="${action.name}_${ui.name}"
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-hover table-striped"
	<#if ui.params.addon?has_content>
		addon="${ui.params.addon}"
	</#if>
	/>
	
	<br/>
	<div class="p-tcenter">
		<#include "bulk-success-buttons.ftl"/>
	</div>
${s}#else>
	<@sback/>
${s}/#if>
	<@safeinc step="_execute"/>
</div>

<@footer/>
