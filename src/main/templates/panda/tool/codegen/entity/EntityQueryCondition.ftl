<#macro condition p>
	/**
	 * @return condition of ${p.name}
	 */
	<#if p.fieldKind == "boolean"> 
	public BooleanCondition<${name}> ${p.name}() {
		return new BooleanCondition<${name}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "date"> 
	public ComparableCondition<${name}, ${p.simpleJavaType}> ${p.name}() {
		return new ComparableCondition<${name}, ${p.simpleJavaType}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "number"> 
	public ComparableCondition<${name}, ${p.simpleJavaType}> ${p.name}() {
		return new ComparableCondition<${name}, ${p.simpleJavaType}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "string"> 
	public StringCondition<${name}> ${p.name}() {
		return new StringCondition<${name}>(this, "${p.name}");
	}
	<#else>
	public ObjectCondition<${name}> ${p.name}() {
		return new ObjectCondition<${name}>(this, "${p.name}");
	}
	</#if> 

</#macro>
