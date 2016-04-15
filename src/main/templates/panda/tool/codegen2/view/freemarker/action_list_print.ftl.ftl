<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	${s}#include "/action-alert.ftl"/>

	${s}#assign _columns_ = [{
			"name": "_number_",
			"type": "number",
			"header": a.getText("listview-th-number", ""),
			"fixed": true
		}] />
<#list ui.displayColumnList as c>
${s}#if a.displayField("${c.name}")>
	${s}#assign _columns_ = _columns_ + [{
			"name": "${c.name}",
			"value": false,
			"header": a.getText("a.t.${c.name}"),
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
			"tooltip": a.getText("a.t.${c.name}-tip", "")
		}] />
${s}/#if>
</#list>


	${s}@p.listview id="${action.name}_${ui.name}" action="~/${ui.name}" 
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-striped" autosize="false" script="false"
	/>
<#if ui.safeInclude??>
	${s}@safeinclude path="<#if ui.safeInclude?has_content>${ui.safeInclude}<#else>${action.simpleActionClass}_${ui.name}_print-custom.ftl</#if>"/>
</#if>
</div>

<@footer/>
