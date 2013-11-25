<#include "common.ftl"/>
<!DOCTYPE validators PUBLIC
		"-//Apache Struts//XWork Validator 1.0.3//EN"
		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">

<validators>
<#list action.propertyList as p>
	<field name="${p.name}">
	<#list p.validatorList as v>
		<field-validator type="${v.type}">
		<#if v.paramList?has_content><#list v.paramList as vp>
			<param name="${vp.name?html}">${vp.value?html}</param>
		</#list></#if>
			<message key="${v.message?html}"/>
		</field-validator>
	</#list>
	<#assign type = p.simpleJavaType/>
	<#if type?ends_with('[]')>
		<#assign type = type?substring(0, type?length - 2)/>
	<#elseif type?ends_with('>') && type?index_of('<') gt 0> 
		<#assign ilt = type?index_of('<')/>
		<#assign type = type?substring(ilt + 1, type?length - 1)/>
	</#if>
		<field-validator type="conversion">
			<message key="validation-conversion-${type?html}"/>
		</field-validator>
	</field>
</#list>
</validators>
