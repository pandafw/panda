<#include "common.ftl"/>
<#include "EntityQueryCondition.ftl"/>
package ${package};

<#list imports as i>
import ${i};
</#list>

public class ${name} extends ${class_name(entity.baseQueryClass)}<${name}> {
	/**
	 * Constructor
	 */
	public ${name}() {
		super();
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public ${name}(Query query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
<#list entity.columnList as p>
	<@condition p=p/>
</#list>
}

