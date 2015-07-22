<#include "common.ftl"/>
<@sheader/>

<div class="p-section">
	<@sheader/>
	<#include "success-toolbar.ftl"/>

	${s}#include "/panda/exts/struts2/views/action-alert.ftl"/>
	<br/>
	
	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, <#rt/>
<#list ui.orderedColumnList as c>
{
		"name": "${c.name}",
		"header": action.getText("${ui.name}-column-${c.name}", ""), 
	<#if c.format??>
		"format": {
			"type": "${c.format.type?replace('#', '\\x23')}"<#if c.format.paramList?has_content>,</#if>
		<#list c.format.paramList as fp>
			"${fp.name}": "${fp.value?replace('#', '\\x23')}"<#if fp_has_next>,</#if>
		</#list>
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
		"sortable": false,
		"tooltip": action.getText("${ui.name}-column-${c.name}-tip", ""),
		"value": false		
	}<#if c_has_next>, </#if><#rt/>
</#list>
] />

	${s}@p.listview id="${action.name}_${ui.name}"
		list="${actionDataListFieldName}" columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
	>
		<#if ui.params.addon?has_content>
		${s}@s.param name="addon">${ui.params.addon}${s}/@s.param>
		</#if>
	${s}/@p.listview>
	
	<br/>
	<div class="p-tcenter">
		<#include "bulk-success-buttons.ftl"/>
	</div>
</div>

<@footer/>
