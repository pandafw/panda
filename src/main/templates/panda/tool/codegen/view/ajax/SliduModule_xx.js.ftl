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
