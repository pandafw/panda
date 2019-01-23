<#list ui.displayFieldList as f>
	${s}#if a.displayField("${f.name}")>
	<#if f.before?has_content>
			${f.before}
	</#if>
	<#if f.content?has_content>
			${f.content}
	<#elseif f.editTag?? && f.editTag.name?ends_with(".hidden")>
			${s}@${f.editTag.name}
				name="${f.name}"
				value="%{r.${f.name}}"
		<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			<#elseif tp.name?starts_with('*')>
				${tp.name?substring(1)}="${tp.value}"
		</#if></#list>
			/>
	<#elseif f.editTag??>
			${s}@${f.editTag.name}
				key="${f.name}"
			<#if f.editTag.name?ends_with(".file")>
				value=""
			<#else>
				value="%{r.${f.name}}"
			</#if>
			<#if (f.required!false)>
				required="true"
			</#if>
			<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			<#elseif tp.name?starts_with('*')>
				${tp.name?substring(1)}="${tp.value}"
			</#if></#list>
			<#if f.editTag.hasParamStartsWithAny("%+")>
			>
				<#list f.editTag.paramList as tp>
			<#if tp.name == '+defaultLink' || tp.name == '_defaultText'>
			<#elseif tp.name?starts_with('%')>
				${s}@p.param name="${tp.name?substring(1)}"><@aurl au=tp.values/>${s}/@p.param>
			<#elseif tp.name?starts_with('+')>
				${s}@p.param name="${tp.name?substring(1)}">${tp.value}${s}/@p.param>
			</#if>
				</#list>
			${s}/@${f.editTag.name}>
			<#else>
			/>
			</#if>
	</#if>
	<#if f.after?has_content>
			${f.after}
	</#if>
	${s}/#if>
</#list>
