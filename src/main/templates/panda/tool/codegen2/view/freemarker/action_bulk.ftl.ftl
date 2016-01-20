<#include "common.ftl"/>
<@header/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<#include "list-star-toolbar.ftl"/>

	${s}#include "/action-alert.ftl"/>
	<br/>

${s}#if result?has_content>
	${s}#assign _columns_ = [{
		"name": "_rownum_",
		"type": "rownum",
		"header": text.getText("listview-th-rownum", ""),
		"fixed": true
	}, {
		"name": "_check_",
		"type": "check",
		"fixed": true
	}<#rt/>
<#list ui.orderedColumnList as c>
{
		"name": "${c.name}",
	<#if entity.isPrimaryKey(c.name)>
		"pkey" : true,
		"value": true,
	<#elseif c.value??>
		"value": ${c.value?string},
	</#if>
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
		"sortable": false,
		"tooltip": text.getText("a.t.${c.name}-tip", "")
	}<#if c_has_next>, </#if><#rt/>
</#list>
] />

	${s}@p.listview id="${action.name}_${ui.name}"
		action="~/${ui.name}_execute" method="post"
		list=result columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		cssTable="table-hover table-striped"
	<#if ui.params.addon?has_content>
		addon="${ui.params.addon}"
	</#if>
	/>
	
	<br/>
	<div class="p-tcenter">
		${s}@p.submit icon="icon-${ui.name}-execute" onclick="return ${action.name}_${ui.name}_submit();" theme="simple">${s}@p.text name="button-${ui.name}-execute"/>${s}/@p.submit>
		<#include "bulk-star-buttons.ftl"/>

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

<@footer/>
