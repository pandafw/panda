<#include "common.ftl"/>
package ${actionPackage};

<#list imports as i>
import ${i};
</#list>
<#macro validates ui><#if ui.requiredValidateFieldList?has_content>@Validates({
			@Validate(value=${gen.validatorType('required')}, params="{ fields: ${ui.requiredValidateFields} }", msgId=${gen.validatorMsgId('required')}),
			@Validate(value=${gen.validatorType('visit')})
			})<#else>@Validates</#if></#macro>

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
<#if action.listUIList?has_content><#list action.listUIList as ui><#if ui.generate!false>
<#if ui.templates?seq_contains("list")
	|| ui.templates?seq_contains("list_print")
	|| ui.templates?seq_contains("list_popup")
	|| ui.templates?seq_contains("list_csv")
	|| ui.templates?seq_contains("list_json")
	|| ui.templates?seq_contains("list_xml")
	|| ui.templates?seq_contains("import")
	|| ui.templates?seq_contains("bdelete")
	|| ui.templates?seq_contains("bupdate")
>
		addDisplayFields(<#list ui.orderedColumnList as c>${entity.simpleName}.${c.uname}<#if c_has_next>, </#if></#list>);
	<#break/>
</#if></#if>
</#list></#if>
<#if action.inputUIList?has_content><#list action.inputUIList as ui><#if ui.generate!false>
<#if ui.templates?seq_contains("view")
	|| ui.templates?seq_contains("print")
	|| ui.templates?seq_contains("add")
	|| ui.templates?seq_contains("copy")
	|| ui.templates?seq_contains("edit")
	|| ui.templates?seq_contains("delete")
>
		addDisplayFields(<#list ui.displayFieldList as f><#if f.actionField>"${f.name}"<#else>${entity.simpleName}.${f.uname}</#if><#if f_has_next>, </#if></#list>);
	<#break/>
</#if></#if>
</#list></#if>
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
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
<#elseif ui.templates?seq_contains("list_popup")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list_popup(qr);
	}
	
<#elseif ui.templates?seq_contains("list_print")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
<#elseif ui.templates?seq_contains("list_csv")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.FTL)
	@Err(View.FTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list_csv(qr);
	}
	
<#elseif ui.templates?seq_contains("list_json")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.JSON)
	@Err(View.JSON)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list_json(qr);
	}
	
<#elseif ui.templates?seq_contains("list_xml")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.XML)
	@Err(View.XML)
	public Object ${gen.trimMethodName(ui.name)}(@Param @Validates Queryer qr) {
		return super.list_xml(qr);
	}
	
<#elseif ui.templates?seq_contains("import")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param Arg arg) {
		return super.import_(arg);
	}
	
<#elseif ui.templates?seq_contains("bdelete")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
<#elseif ui.templates?seq_contains("bupdate")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param Map<String, String[]> args) {
		return super.bupdate(args);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_execute(@Param Map<String, String[]> args) {
		return super.bupdate_execute(args);
	}
	
<#elseif ui.templates?seq_contains("bedit")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}() {
		return super.bedit();
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}_input() {
		return super.bedit_input();
	}

	/**
	 * ${ui.name}_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}_confirm() {
		return super.bedit_confirm();
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${ui.name}_execute() {
		return super.bedit_execute();
	}
	
</#if>
</#if></#list></#if>
<#if action.inputUIList?has_content><#list action.inputUIList as ui><#if ui.generate!false>
<#if ui.templates?seq_contains("view")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		return super.view(key);
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok("sftl:~${ui.name}")
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		return super.view_input(data);
	}

<#elseif ui.templates?seq_contains("print")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		return super.print(key);
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok("sftl:~${ui.name}")
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		return super.print_input(data);
	}

<#elseif ui.templates?seq_contains("add")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}() {
		return super.add();
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok("sftl:~${ui.name}")
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		return super.add_input(data);
	}

	/**
	 * ${ui.name}_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.add_confirm(data);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_execute(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.add_execute(data);
	}

<#elseif ui.templates?seq_contains("copy")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		return super.copy(key);
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok("sftl:~${ui.name}")
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		return super.copy_input(data);
	}

	/**
	 * ${ui.name}_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.copy_confirm(data);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_execute(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.copy_execute(data);
	}

<#elseif ui.templates?seq_contains("edit")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		return super.edit(key);
	}

	/**
	 * ${ui.name}_input
	 */
	@At
	@Ok("sftl:~${ui.name}")
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		return super.edit_input(data);
	}

	/**
	 * ${ui.name}_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.edit_confirm(data);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~${ui.name}")
	public Object ${ui.name}_execute(@Param <@validates ui=ui/> ${entityBeanClass} data) {
		return super.edit_execute(data);
	}

<#elseif ui.templates?seq_contains("delete")>
	/**
	 * ${ui.name}
	 */
	@At${gen.trimAtName(ui.name)}
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		return super.delete(key);
	}

	/**
	 * ${ui.name}_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:${ui.name}")
	public Object ${ui.name}_execute(@Param ${entityBeanClass} key) {
		return super.delete_execute(key);
	}

</#if>
</#if></#list></#if>
}

