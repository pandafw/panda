<#include "common.ftl"/>
package ${actionPackage};

<#list imports as i>
import ${i};
</#list>

<#if action.path??>
@At("${action.path}/${action.name}")
</#if>
<#if action.auth?has_content>
@Auth(${action.auth})
</#if>
public<#if !(action.path??)> abstract</#if> class ${actionClass} extends ${actionBaseClass}<${entityBeanClass}> {
<#if action.propertyList?has_content>
	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
<#list action.propertyList as p>
	${p.modifier} ${p.simpleJavaType} ${p.name}<#if p.initValue?has_content> = ${p.initValue}</#if>;
</#list>
</#if>

	/**
	 * Constructor
	 */
	public ${actionClass}() {
		setType(${entityBeanClass}.class);
	}

<#list action.propertyList as p>
	/**
	 * @return the ${p.name}
	 */
	public ${p.simpleJavaType} get${p.name?cap_first}() {
	<#assign getterTrim = "" />
	<#if p.getterTrim??>
		<#if p.getterTrim?has_content>
			<#assign getterTrim = p.getterTrim/>
		</#if>
	</#if>
	<#if p.getterCode?has_content>
		${p.getterCode}
	<#elseif getterTrim?has_content>
		return ${getterTrim}(${p.name});
	<#else>
		return ${p.name};
	</#if>
	}

	/**
	 * @param ${p.name} the ${p.name} to set
	 */
	public void set${p.name?cap_first}(${p.simpleJavaType} ${p.name}) {
	<#assign setterTrim = "" />
	<#if p.setterTrim??>
		<#if p.setterTrim?has_content>
			<#assign setterTrim = p.setterTrim/>
		</#if>
	<#elseif action.trimString?has_content && p.simpleJavaType == "String">
		<#assign setterTrim = action.trimString />
	</#if>
	<#if p.setterCode?has_content>
		${p.setterCode}
	<#elseif setterTrim?has_content>
		this.${p.name} = ${setterTrim}(${p.name});
	<#else>
		this.${p.name} = ${p.name};
	</#if>
	}

</#list>

	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
<#if action.listUIList?has_content><#list action.listUIList as ui><#if ui.generate!false>
<#if ui.templates?seq_contains("list")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
<#elseif ui.templates?seq_contains("list_popup")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list_popup(qr);
	}
	
<#elseif ui.templates?seq_contains("list_csv")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.FTL)
	@Err(View.SFTL)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list_csv(qr);
	}
	
<#elseif ui.templates?seq_contains("list_print")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
<#elseif ui.templates?seq_contains("list_json")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.JSON)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list_json(qr);
	}
	
<#elseif ui.templates?seq_contains("list_xml")>
	/**
	 * ${ui.name}
	 */
	@At
	@Ok(View.XML)
	public Object ${ui.name}(@Param @Validates Queryer qr) {
		return super.list_xml(qr);
	}
	
</#if>
</#if></#list></#if>
}

