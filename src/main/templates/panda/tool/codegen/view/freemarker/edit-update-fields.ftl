<#list ui.displayFieldList as f>
	${s}#if a.displayField("${f.name}")>
	<#if f.before?has_content>
			${f.before}
	</#if>
	<#if f.content?has_content>
			${f.content}
	<#elseif f.editTag?? && f.editTag.name?ends_with(".hidden")>
			${s}@${f.editTag.name}
				name="<#if f.actionField>a.</#if>${f.name}"
				value="%{<#if f.actionField>a<#else>r</#if>.${f.name}}"
		<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			<#elseif tp.name?starts_with('*')>
				${tp.name?substring(1)}="${tp.value}"
		</#if></#list>
			/>
	<#elseif f.editTag??>
		<#if entity.isPrimaryKey(f.name)>
			${s}@p.viewfield
				key="<#if f.actionField>a.</#if>${f.name}"
				value="%{<#if f.actionField>a<#else>r</#if>.${f.name}}"
			<#list f.editTag.paramList as tp>
				<#if [ "fieldValue", "list", "listKey", "listValue", "listBreak", "listOrder" ]?seq_contains(tp.name)>
				${tp.name}="${tp.value}"
				</#if>
			</#list>
				required="true"
			<#if f.editTag.hasParamStartsWithAny("_*")>
			>
				<#list f.editTag.paramList as tp>
					<#if tp.name?starts_with('_') || tp.name?starts_with('*')>
				${s}@p.param name="${tp.name?substring(1)}">${tp.value}${s}/@p.param>
					</#if>
				</#list>
			${s}/@${f.editTag.name}>
			<#else>
			/>
			</#if>
		<#else>
			${s}@${f.editTag.name}
				key="<#if f.actionField>a.</#if>${f.name}"
			<#if f.editTag.name?ends_with(".file")>
				value=""
			<#else>
				value="%{<#if f.actionField>a<#else>r</#if>.${f.name}}"
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
					<#if tp.name?starts_with('%')>
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
	</#if>
	<#if f.after?has_content>
			${f.after}
	</#if>
	${s}/#if>
</#list>
