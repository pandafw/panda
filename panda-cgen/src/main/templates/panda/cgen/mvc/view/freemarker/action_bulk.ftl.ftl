<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name, ui.name + "-confirm" ]/>
	<@swell/>

	<#include "bulk-toolbar.ftl"/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>
	<br/>

${s}#if result?has_content>
	${s}#assign _columns_ = [{
			"name": "_rownum_",
			"type": "rownum",
			"header": a.getText("listview-th-rownum", ""),
			"fixed": true
		}, {
			"name": "_check_",
			"type": "check",
			"fixed": true
		}, <#list ui.orderedColumnList as c>{
			"name": "${c.name}",
		<#if entity.isPrimaryKey(c.name)>
			"pkey" : true,
			"value": true,
		<#elseif c.value??>
			"value": ${c.value?string},
		</#if>
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
		action="./${ui.name}${props['ui.action.seperator']!'.'}execute" method="post"
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-hover table-striped"
	<#if ui.params.addon?has_content>
		addon="${ui.params.addon}"
	</#if>
	/>
	
	<br/>
	<div class="p-tcenter"${gen.focusme(ui)}>
		${s}@p.submit onclick="return ${action.name}_${ui.name}_submit();" icon="icon-${ui.name}-execute" label="#(btn-${ui.name}-execute)"/>
		<#include "bulk-buttons.ftl"/>

		<script type="text/javascript"><!--
			function ${action.name}_${ui.name}_submit() {
				return plv_submitCheckedKeys('${action.name}_${ui.name}');
			}
			
			function onPageLoad() {
				plv_checkAll('${action.name}_${ui.name}');
			}
		--></script>
	</div>
${s}#else>
	<@sback/>
${s}/#if>
</div>

<@footinc step=""/>
<@footer/>
