<#include "common.ftl"/>
<#include "EntityQueryCondition.ftl"/>
package ${entityQueryPackage};

<#list imports as i>
import ${i};
</#list>

public class ${entityQueryClass} extends ${class_name(model.baseExampleClass)}<${entityQueryClass}> {
	/**
	 * Constructor
	 */
	public ${entityQueryClass}() {
		super();
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
<#list model.columnList as p>
	<@condition p=p/>
</#list>
}

