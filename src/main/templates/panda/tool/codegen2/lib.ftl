<#function package_name clazz>
	<#local dot = clazz?last_index_of('.')/>
	<#if dot gt 1 >
		<#return clazz?substring(0, dot)/>
	<#else>
		<#return ""/>
	</#if>
</#function>
<#function class_name clazz>
	<#local dot = clazz?last_index_of('.')/>
	<#if dot gte 0 >
		<#return clazz?substring(dot + 1, clazz?length)/>
	<#else>
		<#return ""/>
	</#if>
</#function>