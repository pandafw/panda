<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader/>
	<#include "list-star-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>
	<br/>

	${s}#assign _columns_ = [{
		"name": "_number_",
		"type": "number",
		"nowrap": true,
		"fixed": true
	}, {
		"name": "_check_",
		"type": "check",
		"nowrap": true,
		"fixed": true
	}<#rt/>
<#list ui.orderedColumnList as c>
{
		"name": "${c.name}",
	<#if entity.isPrimaryKey(c.name)>
		"pkey" : true,
	</#if>
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
		"tooltip": action.getText("${ui.name}-column-${c.name}-tip", "")			
	}<#if c_has_next>, </#if><#rt/>
</#list>
] />

	${s}@p.listview id="${action.name}_${ui.name}"
		action="${action.name}_${ui.name}_execute" method="post"
		list="${actionDataListFieldName}" columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
	>
		<#if ui.params.addon?has_content>
		${s}@s.param name="addon">${ui.params.addon}${s}/@s.param>
		</#if>
	${s}/@p.listview>
	
	<br/>
	<div class="p-tcenter">
		${s}@p.submit icon="icon-${ui.name}-execute" onclick="return ${action.name}_${ui.name}_submit();" theme="simple">${s}@p.text name="button-${ui.name}-execute"/>${s}/@p.submit>
		<#include "bulk-star-buttons.ftl"/>

		<script type="text/javascript"><!--
			function ${action.name}_${ui.name}_submit() {
				return nlv_submitCheckedKeys('${action.name}_${ui.name}');
			}
			
			$(function() {
				nlv_checkAll('${action.name}_${ui.name}');
			});
		--></script>
	</div>
</div>

<@footer/>
