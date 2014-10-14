<#include "common.ftl"/>
package ${actionPackage};

<#list imports as i>
import ${i};
</#list>

public class ${actionClass} extends ${actionBaseClass}<${entityBeanClass}> {
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

	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the ${entityBeanClass?uncap_first}
	 */
	public ${entityBeanClass} get${actionDataFieldName?cap_first}() {
		return super.getData();
	}

	/**
	 * @param ${entityBeanClass?uncap_first} the ${entityBeanClass?uncap_first} to set
	 */
	public void set${actionDataFieldName?cap_first}(${entityBeanClass} ${entityBeanClass?uncap_first}) {
		super.setData(${entityBeanClass?uncap_first});
	}

	/**
	 * @return the ${entityBeanClass?uncap_first} list
	 */
	public List<${entityBeanClass}> get${actionDataListFieldName?cap_first}() {
		return super.getDataList();
	}

	/**
	 * @param ${entityBeanClass?uncap_first}List the ${entityBeanClass?uncap_first}List to set
	 */
	public void set${actionDataListFieldName?cap_first}(List<${entityBeanClass}> ${entityBeanClass?uncap_first}List) {
		super.setDataList(${entityBeanClass?uncap_first}List);
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
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.list();
	}
	
<#elseif ui.templates?seq_contains("list_popup")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.list_popup();
	}
	
<#elseif ui.templates?seq_contains("list_csv")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.list_csv();
	}
	
<#elseif ui.templates?seq_contains("list_print")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.list_print();
	}
	
<#elseif ui.templates?seq_contains("bdelete")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.bdelete();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.bdelete_execute();
	}
	
<#elseif ui.templates?seq_contains("bupdate")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.bupdate();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.bupdate_execute();
	}
	
<#elseif ui.templates?seq_contains("bedit")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.bedit();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.bedit_input();
	}

	/**
	 * ${ui.name}_confirm
	 * @return SUCCESS
	 */
	public String ${ui.name}_confirm() {
		return super.bedit_confirm();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.bedit_execute();
	}
	
</#if>
</#if></#list></#if>
<#if action.inputUIList?has_content><#list action.inputUIList as ui><#if ui.generate!false>
<#if ui.templates?seq_contains("view")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.view();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.view_input();
	}

<#elseif ui.templates?seq_contains("print")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.print();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.print_input();
	}

<#elseif ui.templates?seq_contains("insert")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.insert();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.insert_input();
	}

	/**
	 * ${ui.name}_confirm
	 * @return SUCCESS
	 */
	public String ${ui.name}_confirm() {
		return super.insert_confirm();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.insert_execute();
	}

<#elseif ui.templates?seq_contains("copy")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.copy();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.copy_input();
	}

	/**
	 * ${ui.name}_confirm
	 * @return SUCCESS
	 */
	public String ${ui.name}_confirm() {
		return super.copy_confirm();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.copy_execute();
	}

<#elseif ui.templates?seq_contains("update")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.update();
	}

	/**
	 * ${ui.name}_input
	 * @return SUCCESS
	 */
	public String ${ui.name}_input() {
		return super.update_input();
	}

	/**
	 * ${ui.name}_confirm
	 * @return SUCCESS
	 */
	public String ${ui.name}_confirm() {
		return super.update_confirm();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.update_execute();
	}

<#elseif ui.templates?seq_contains("delete")>
	/**
	 * ${ui.name}
	 * @return SUCCESS
	 */
	public String ${ui.name}() {
		return super.delete();
	}

	/**
	 * ${ui.name}_execute
	 * @return SUCCESS
	 */
	public String ${ui.name}_execute() {
		return super.delete_execute();
	}

</#if>
</#if></#list></#if>
}