<#include "common.ftl"/>
package ${package};

<#list imports as i>
import ${i};
</#list>

<#if entity.comment?has_content>
@Comment("${entity.comment}")
</#if>
<#if entity.table?has_content>
@Table("${entity.table}")
</#if>
<#if entity.foreignKeyMap?has_content>
@ForeignKeys({
<#list entity.foreignKeyMap?keys as k>
	@FK(${entity.getForeignKeyDefine(entity.foreignKeyMap[k])})<#if k_has_next>,</#if>
</#list>
})
</#if>
<#if entity.uniqueKeyMap?has_content>
@Indexes({
<#list entity.uniqueKeyMap?keys as k>
	@Index(name="${k}", fields={ <#list entity.uniqueKeyMap[k] as p>"${p.name}"<#if p_has_next>,</#if></#list> }, unique=true)<#if k_has_next>,</#if>
</#list>
})
</#if>
<#if entity.joinMap?has_content>
@Joins({
<#list entity.joinMap?keys as k>
	@Join(${entity.getJoinDefine(entity.joinMap[k])})<#if k_has_next>,</#if>
</#list>
})
</#if>
public class ${name} <#if entity.baseBeanClass?has_content>extends ${class_name(entity.baseBeanClass)}<#else>implements Serializable</#if> {

	private static final long serialVersionUID = ${svuid?c}L;

	/**
	 * Constructor
	 */
	public ${name}() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
<#list entity.propertyList as p>
	public static final String ${p.uname} = "${p.name}";

</#list>

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
<#list entity.propertyList as p>
<#if entity.isIdentity(p.name)>
	@Id<#if entity.identityDefine?has_content>(${entity.identityDefine})</#if>
<#elseif p.dbColumn>
	<#if p.primaryKey>
	@PK
	</#if>
	@Column<#if p.columnDefine?has_content>(${p.columnDefine})</#if>
<#elseif p.joinColumn>
	@JoinColumn(name="${p.joinName}", field="${p.joinField}")
</#if>
<#if p.comment?has_content>
	@Comment("${p.comment}")
</#if>
	${p.modifier} ${p.simpleJavaType} ${p.name}<#if p.initValue?has_content> = ${p.initValue}</#if>;

</#list>

	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
<#list entity.propertyList as p>
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
	<#elseif entity.trimString?has_content && p.simpleJavaType == "String">
		<#assign setterTrim = entity.trimString />
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

	/**
	 * set properties from the specified object.
	 */
	public void set(${name} src) {
<#list entity.propertyList as p>
		this.${p.name} = src.${p.name};
</#list>
<#if entity.baseBeanClass?has_content>
		super.set(src);
</#if>
	}

	/*----------------------------------------------------------------------*
	 * Overrides
	 *----------------------------------------------------------------------*/
	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public ${name} clone() {
		${name} copy = new ${name}();
		
		copy.set(this);

		return copy;
	}

	/**
	 * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
<#list entity.primaryKeyList as p>
				.append(${p.name})
</#list>
				.toHashCode();
	}

	/**
	 * @return  <code>true</code> if this object is the same as the obj argument; 
	 * 			<code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		${name} rhs = (${name})obj;
<#if 'false' == props['entity.equals.pkey']!'true'>
	<#assign pl = entity.propertyList/>
		return Objects.equalsBuilder()
<#list pl as p>
				.append(${p.name}, rhs.${p.name})
</#list>
<#if entity.baseBeanClass?has_content>
				.appendSuper(super.equals(rhs))
</#if>
<#else>
	<#assign pl = entity.primaryKeyList/>
		return Objects.equalsBuilder()
<#list pl as p>
				.append(${p.name}, rhs.${p.name})
</#list>
</#if>		
				.isEquals();
	}

	/**
	 * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
<#list entity.propertyList as p>
				.append(${p.uname}, ${p.name})
</#list>
<#if entity.baseBeanClass?has_content>
				.appendSuper(super.toString())
</#if>
				.toString();
	}
}

