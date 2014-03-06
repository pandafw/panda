<#include "common.ftl"/>
<#include "EntityQueryCondition.ftl"/>
package ${package};

<#list imports as i>
import ${i};
</#list>

public class ${name} extends ${class_name(entity.baseQueryClass)}<${entity.simpleName}, ${name}> {
	/**
	 * Constructor
	 */
	public ${name}() {
		super(${entity.simpleName}.class);
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
<#list entity.columnList as p>
	<@condition p=p/>
</#list>
}

