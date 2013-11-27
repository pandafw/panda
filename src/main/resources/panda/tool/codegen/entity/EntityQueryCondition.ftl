<#macro condition p>
	/**
	 * @return condition of ${p.name}
	 */
	<#if p.fieldKind == "boolean"> 
	public BooleanCondition<${entityQueryClass}> ${p.name}() {
		return new BooleanCondition<${entityQueryClass}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "date"> 
	public ComparableCondition<${entityQueryClass}, ${p.simpleJavaType}> ${p.name}() {
		return new ComparableCondition<${entityQueryClass}, ${p.simpleJavaType}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "number"> 
	public ComparableCondition<${entityQueryClass}, ${p.simpleJavaType}> ${p.name}() {
		return new ComparableCondition<${entityQueryClass}, ${p.simpleJavaType}>(this, "${p.name}");
	}
	<#elseif p.fieldKind == "string"> 
	public LikeableCondition<${entityQueryClass}, ${p.simpleJavaType}> ${p.name}() {
		return new LikeableCondition<${entityQueryClass}, ${p.simpleJavaType}>(this, "${p.name}");
	}
	<#else>
	public ObjectCondition<${entityQueryClass}> ${p.name}() {
		return new ObjectCondition<${entityQueryClass}>(this, "${p.name}");
	}
	</#if> 

</#macro>
