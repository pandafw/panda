<#include "common.ftl"/>
<!DOCTYPE validators PUBLIC
		"-//Apache Struts//XWork Validator 1.0.3//EN"
		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">

<validators>
<#list entity.propertyList as p>
	<#assign type = p.simpleJavaType/>
	<#if type?ends_with('[]')>
		<#assign type = type?substring(0, type?length - 2)/>
	<#elseif type?ends_with('>') && type?index_of('<') gt 0> 
		<#assign ilt = type?index_of('<')/>
		<#assign type = type?substring(ilt + 1, type?length - 1)/>
	</#if>
	<#if p.validatorList?has_content || type != "String">
	<field name="${p.name}">
	<#list p.validatorList as v>
		<field-validator type="${v.type}">
		<#if v.paramList?has_content><#list v.paramList as vp>
			<param name="${vp.name}">${vp.value}</param>
		</#list></#if>
			<message key="${v.message}"/>
		</field-validator>
	</#list>
	<#if type != "String">
		<field-validator type="conversion">
			<message key="validation-conversion-${type?html}"/>
		</field-validator>
	</#if>
	</field>
	</#if>
</#list>
</validators>
