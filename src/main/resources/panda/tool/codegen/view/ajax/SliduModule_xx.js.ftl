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
<#assign labels = {} />
<#assign tooltips = {} />
<#list model.propertyList as p>
	<#if p.label?has_content><#assign labels = labels + { p.name: (p.label!"") }/></#if>
	<#if p.tooltip?has_content><#assign tooltips = tooltips + { p.name: (p.tooltip!"") }/></#if>
</#list>
<#list action.propertyList as p>
	<#if p.label?has_content><#assign labels = labels + { p.name: (p.label!"") }/></#if>
	<#if p.tooltip?has_content><#assign tooltips = tooltips + { p.name: (p.tooltip!"") }/></#if>
</#list>
Nexts.Resources.bind("${props['ajax.basePackage']}.${action.package}.${action.name?cap_first}Module", "${locale?js_string}", {
	title:   "${(action.title!'')?js_string}",
	tooltip: "<b>${(action.title!"")?js_string}</b>",
	
	fieldLabel: {
	<#list labels?keys as k>
		"${k?js_string}": "${labels[k]?js_string}"<#if k_has_next>,</#if>
	</#list>
	},
	
	fieldTootip: {
	<#list tooltips?keys as k>
		"${k?js_string}": "${tooltips[k]?js_string}"<#if k_has_next>,</#if>
	</#list>
	}
});
