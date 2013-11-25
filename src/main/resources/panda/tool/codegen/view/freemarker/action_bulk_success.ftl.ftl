<#--
/*
 * This file is part of Nuts Framework.
 * Copyright(C) 2009-2012 Nuts Develop Team.
 *
 * Nuts Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 *
 * Nuts Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuts Framework. If not, see <http://www.gnu.org/licenses/>.
 */
-->
<#include "common.ftl"/>
<@header/>

<div class="n-sect">
	<div class="n-sect-head">
		<#include "success-toolbar.ftl"/>
		<h3 class="n-sect-title">${s}@n.text name="title-${d}{actionResult}">${s}@s.param>${s}@n.text name="title"/>${s}/@s.param>${s}/@n.text></h3>
	</div>
	<div class="n-sect-body">
		${s}#include "/nuts/exts/struts2/views/action-prompt.ftl"/>
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

		${s}@n.listview id="${action.name}_${ui.name}"
			list="${actionDataListFieldName}" columns=_columns_<#if ui.cssColumn?has_content> cssColumn="${ui.cssColumn}"</#if>
		>
			<#if ui.params.addon?has_content>
			${s}@s.param name="addon">${ui.params.addon}${s}/@s.param>
			</#if>
		${s}/@n.listview>
		
		<br/>
		<div class="n-tcenter">
			<#include "bulk-success-buttons.ftl"/>
		</div>
	</div>
</div>

<@footer/>
