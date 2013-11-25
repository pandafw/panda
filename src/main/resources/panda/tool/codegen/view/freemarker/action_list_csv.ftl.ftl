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
${s}#if action.hasErrors()>
<@header/>

<div class="n-sect">
	<div class="n-sect-head">
		<h3 class="n-sect-title">${s}@n.text name="title-${d}{actionResult}">${s}@s.param>${s}@n.text name="title"/>${s}/@s.param>${s}/@n.text></h3>
	</div>
	<div class="n-sect-body">
		${s}#include "/nuts/exts/struts2/views/action-prompt-all.ftl"/>
	</div>
</div>

<@footer/>
${s}#else>
${s}@n.text var="_fn_" name="title-${d}{actionResult}">${s}@s.param>${s}@n.text name="title"/>${s}/@s.param>${s}/@n.text>
${s}@n.head charset="UTF-8" bom="true" contentType="text/comma-separated-values"
	noCache="true" attachment="true" filename="%{#_fn_ + '.csv'}"/>
${s}#assign _columns_ = [<#rt/>
<#list ui.displayColumnList as c>
{
	"name": "${c.name}",
	"header": action.getText("${ui.name}-column-${c.name}"), 
<#if c.format??>
	"format": {
	<#list c.format.paramList as fp>
		"${fp.name}": "${fp.value?replace('#', '\\x23')}",
	</#list>
		"type": "${c.format.type?replace('#', '\\x23')}"
	},
</#if>
	"hidden": ${(c.hidden!false)?string}
}<#if c_has_next>, </#if><#rt/>
</#list>
] />
${s}@n.csv list="${actionDataListFieldName}" columns=_columns_/>
${s}/#if>