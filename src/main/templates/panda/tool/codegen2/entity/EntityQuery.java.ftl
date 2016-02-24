<#include "common.ftl"/>
package ${package};

<#list imports as i>
import ${i};
</#list>

public class ${name} extends ${class_name(entity.baseQueryClass)}<${entity.simpleName}, ${name}> {
	/**
	 * Constructor
	 */
	public ${name}() {
		super(Entities.i().getEntity(${entity.simpleName}.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public ${name}(GenericQuery<${entity.simpleName}> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
<#list entity.propertyList as p><#if p.dbColumn || p.joinColumn>
	/**
	 * @return condition of ${p.name}
	 */
	<#if p.fieldKind == "boolean">
	public BooleanCondition<${name}> ${p.name}() {
		return new BooleanCondition<${name}>(this, ${entity.simpleName}.${p.uname});
	}
	<#elseif p.fieldKind == "date">
	public ComparableCondition<${name}, ${p.simpleJavaWrapType}> ${p.name}() {
		return new ComparableCondition<${name}, ${p.simpleJavaWrapType}>(this, ${entity.simpleName}.${p.uname});
	}
	<#elseif p.fieldKind == "number">
	public ComparableCondition<${name}, ${p.simpleJavaWrapType}> ${p.name}() {
		return new ComparableCondition<${name}, ${p.simpleJavaWrapType}>(this, ${entity.simpleName}.${p.uname});
	}
	<#elseif p.fieldKind == "string">
	public StringCondition<${name}> ${p.name}() {
		return new StringCondition<${name}>(this, ${entity.simpleName}.${p.uname});
	}
	<#else>
	public ObjectCondition<${name}> ${p.name}() {
		return new ObjectCondition<${name}>(this, ${entity.simpleName}.${p.uname});
	}
	</#if> 

</#if></#list>

<#if entity.joinMap?has_content>
	//----------------------------------------------------------------------
	// auto joins
	//----------------------------------------------------------------------
<#list entity.joinMap?keys as k>
	/**
	 * auto left join ${k}
	 */
	public ${name} autoLeftJoin${k}() {
		autoLeftJoin(${entity.simpleName}._JOIN_${k?upper_case}_);
		return this;
	}

	/**
	 * auto right join ${k}
	 */
	public ${name} autoRightJoin${k}() {
		autoRightJoin(${entity.simpleName}._JOIN_${k?upper_case}_);
		return this;
	}

	/**
	 * auto inner join ${k}
	 */
	public ${name} autoInnerJoin${k?upper_case}() {
		autoInnerJoin(${entity.simpleName}._JOIN_${k?upper_case}_);
		return this;
	}

	/**
	 * auto join ${k}
	 */
	public ${name} autoJoin${k}() {
		autoJoin(${entity.simpleName}._JOIN_${k?upper_case}_);
		return this;
	}

</#list>
</#if>
}

