<#include "common.ftl"/>
<!DOCTYPE validators PUBLIC
		"-//Apache Struts//XWork Validator 1.0.3//EN"
		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">

<validators>
	<#list entity.primaryKeyList as p>
	<field name="${actionDataFieldName}.${p.name}">
		<field-validator type="required<#if p.simpleJavaType == 'String'>string</#if>">
			<message key="validation-required<#if p.simpleJavaType == 'String'>string</#if>"/>
		</field-validator>
		<#if p.simpleJavaType != "String">
		<field-validator type="conversion">
			<message key="validation-conversion-${p.simpleJavaType?html}"/>
		</field-validator>
		</#if>
	</field>
	</#list>
</validators>