<#include "common.ftl"/>
package ${actionPackage};

<#list imports as i>
import ${i};
</#list>
<#macro setListDisplayColumns ui><#assign alcs = action.getDisplayListColumns(entity)/><#assign lcs = ui.getDisplayColumns(entity)/>
<#if !(alcs?has_content) && lcs?has_content>
		setDisplayFields(${lcs});
</#if>
</#macro>
<#macro setInputDisplayFields ui><#assign aifs = action.getDisplayInputFields(entity)/><#assign ifs = ui.getDisplayFields(entity)/>
<#if !(aifs?has_content) && ifs?has_content>
		setDisplayFields(${ifs});
</#if>
</#macro>
<#macro validates ui><#if ui.requiredValidateFieldList?has_content>

			@RequiredValidate(${ui.requiredValidateFields})
			@VisitValidate
			<#else>@VisitValidate </#if></#macro>

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
<#assign lcs = action.getDisplayListColumns(entity)/>
<#if lcs?has_content>
		setDisplayFields(${lcs});
</#if>
<#assign ifs = action.getDisplayInputFields(entity)/>
<#if ifs?has_content>
		setDisplayFields(${ifs});
</#if>
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

<#if action.autoJoin?has_content>
	/*----------------------------------------------------------------------*
	 * Joins
	 *----------------------------------------------------------------------*/
	/**
	 * add query joins
	 * @param dq data query
	 */
	@Override
	protected void addQueryJoins(DataQuery<${entityBeanClass}> dq) {
		super.addQueryJoins(dq);

		${entityBeanClass}Query eq = new ${entityBeanClass}Query(dq);
<#if action.autoJoin == "all">
	<#list entity.joinMap?keys as k>
		eq.autoJoin${k?upper_case}();
	</#list>
<#elseif action.autoJoin == "inner:all">
	<#list entity.joinMap?keys as k>
		eq.autoInnerJoin${k?upper_case}();
	</#list>
<#elseif action.autoJoin == "left:all">
	<#list entity.joinMap?keys as k>
		eq.autoLeftJoin${k?upper_case}();
	</#list>
<#elseif action.autoJoin == "right:all">
	<#list entity.joinMap?keys as k>
		eq.autoInnerJoin${k?upper_case}();
	</#list>
<#else>
	<#list action.autoJoins?keys as k>
		eq.auto${action.autoJoins[k]}Join${k?upper_case}();
	</#list>
</#if>
	}

</#if>
	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
<#if action.listUIList?has_content><#list action.listUIList as ui><#if ui.generate!false>
<#if ui.template == "list">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer qr) {
		<@setListDisplayColumns ui=ui/>
		return super.list(qr);
	}
	
<#elseif ui.template == "list_popup">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer qr) {
		<@setListDisplayColumns ui=ui/>
		return super.list_popup(qr);
	}
	
<#elseif ui.template == "list_print">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer qr) {
		<@setListDisplayColumns ui=ui/>
		return super.list_print(qr);
	}
	
<#elseif [ "list_csv", "list_tsv", "list_xls", "list_xlsx", "expo_csv", "expo_tsv", "expo_xls", "expo_xlsx" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer<#if ui.template?starts_with('expo_')>Ex</#if> qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
<#list ui.orderedColumnList as c>
		if (displayField(<#if c.virtualColumn>"${c.name}"<#else>${entity.simpleName}.${c.uname}</#if>)) {
			ListColumn lc = new ListColumn();
			lc.name = <#if c.virtualColumn>"${c.name}"<#else>${entity.simpleName}.${c.uname}</#if>;
			lc.header = getFieldLabel(<#if c.virtualColumn>"${c.name}"<#else>${entity.simpleName}.${c.uname}</#if>);
			lc.hidden = ${(c.hidden!false)?string};
	<#if c.format??>
			ListColumn.Format lcf = new ListColumn.Format();
		<#if c.format.type?has_content>
			lcf.type = "${c.format.type}";
		</#if>
		<#if c.format.pattern?has_content>
			lcf.pattern = "${c.format.pattern}";
		</#if>
		<#list c.format.paramList as fp>
			lcf.${fp.name} = ${gen.translateToJava(fp.value)};
		</#list>
			lc.format = lcf;
	</#if>
			columns.add(lc);
		}
</#list>
		return super.${ui.template}(qr, columns);
	}
	
<#elseif [ "list_json", "list_xml", "expo_json", "expo_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer<#if ui.template?starts_with('expo_')>Ex</#if> qr) {
		<@setListDisplayColumns ui=ui/>
		return super.${ui.template}(qr);
	}
	
<#elseif [ "list_pdf", "expo_pdf" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param qr queryer
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param @VisitValidate Queryer<#if ui.template?starts_with('expo_')>Ex</#if> qr) {
		<@setListDisplayColumns ui=ui/>
		return super.${ui.template}(qr);
	}
	
<#elseif ui.template == "import">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param arg argument
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object ${gen.trimMethodName(ui.name)}(@Param Arg arg) {
		<@setListDisplayColumns ui=ui/>
		return super.importx(arg);
	}
	
<#elseif ui.template == "bdelete">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param args arguments
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param Map<String, String[]> args) {
		<@setListDisplayColumns ui=ui/>
		return super.bdelete(args);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param Map<String, String[]> args) {
		<@setListDisplayColumns ui=ui/>
		return super.bdelete_execute(args);
	}
	
<#elseif ui.template == "bupdate">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param args arguments
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param Map<String, String[]> args) {
		<@setListDisplayColumns ui=ui/>
		return super.bupdate(args);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param Map<String, String[]> args) {
		<@setListDisplayColumns ui=ui/>
		return super.bupdate_execute(args);
	}
	
<#elseif ui.template == "bedit">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}() {
		<@setListDisplayColumns ui=ui/>
		return super.bedit();
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${ui.name}_input() {
		<@setListDisplayColumns ui=ui/>
		return super.bedit_input();
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_confirm
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object ${ui.name}_confirm() {
		<@setListDisplayColumns ui=ui/>
		return super.bedit_confirm();
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object ${ui.name}_execute() {
		<@setListDisplayColumns ui=ui/>
		return super.bedit_execute();
	}
	
</#if>
</#if></#list></#if>
<#if action.inputUIList?has_content><#list action.inputUIList as ui><#if ui.generate!false>
<#if ui.template == "view">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.view(key);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~${gen.trimMethodName(ui.name)}", error="sftl:~${gen.trimMethodName(ui.name)}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.view_input(data);
	}

<#elseif [ "view_json", "view_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.view(key);
	}

<#elseif ui.template == "print">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.print(key);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~${gen.trimMethodName(ui.name)}", error="sftl:~${gen.trimMethodName(ui.name)}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.print_input(data);
	}

<#elseif ui.template == "add">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}() {
		<@setInputDisplayFields ui=ui/>
		return super.add();
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~${gen.trimMethodName(ui.name)}", error="sftl:~${gen.trimMethodName(ui.name)}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.add_input(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.add_confirm(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.add_execute(data);
	}

<#elseif [ "add_json", "add_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param data the input data
	 * @return result
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.add_execute(data, true);
	}

<#elseif ui.template == "copy">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.copy(key);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~${gen.trimMethodName(ui.name)}", error="sftl:~${gen.trimMethodName(ui.name)}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.copy_input(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.copy_confirm(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.copy_execute(data);
	}

<#elseif [ "copy_json", "copy_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param data the input data
	 * @return result
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.copy_execute(data, true);
	}

<#elseif ui.template == "edit">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.edit(key);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~${gen.trimMethodName(ui.name)}", error="sftl:~${gen.trimMethodName(ui.name)}")
	public Object ${ui.name}_input(@Param ${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.edit_input(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_confirm(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.edit_confirm(data);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${gen.trimMethodName(ui.name)}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.edit_execute(data);
	}

<#elseif [ "edit_json", "edit_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param data the input data
	 * @return result
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param <@validates ui=ui/>${entityBeanClass} data) {
		<@setInputDisplayFields ui=ui/>
		return super.edit_execute(data, true);
	}

<#elseif ui.template == "delete">
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.trimAtName(ui.name)}
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.delete(key);
	}

	/**
	 * ${gen.trimMethodName(ui.name)}_execute
	 * @param key the input key
	 * @return result or view
	 */
	@At${gen.postAtName(ui.name)}
	@To(value=Views.SFTL, error="sftl:~${ui.name}")
	@TokenProtect
	public Object ${ui.name}_execute(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.delete_execute(key);
	}

<#elseif [ "delete_json", "delete_xml" ]?seq_contains(ui.template)>
	/**
	 * ${gen.trimMethodName(ui.name)}
	 * @param key the input key
	 * @return result
	 */
	@At${gen.trimAtName(ui.name)}
	@To(Views.S${ui.template?keep_after('_')?upper_case})
	public Object ${gen.trimMethodName(ui.name)}(@Param ${entityBeanClass} key) {
		<@setInputDisplayFields ui=ui/>
		return super.delete_execute(key, true);
	}

</#if>
</#if></#list></#if>
}

