<#list ui.displayFieldList as f>
	<#if f.before?has_content>
			${f.before}
	</#if>
	<#if f.content?has_content>
			${f.content}
	<#elseif f.editTag?? && f.editTag.name?ends_with(".hidden")>
			${s}@${f.editTag.name}
			<#if f.editTag.cssClass??>
				cssClass="${f.editTag.cssClass}"
			</#if>
				name="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
		<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
		</#if></#list>
			/>
	<#elseif f.editTag??>
		<#if entity.isPrimaryKey(f.name)>
			${s}@p.viewfield
				key="${actionDataFieldName}.${f.name}"
			<#list f.editTag.paramList as tp>
				<#if tp.name == "list" || tp.name == "listKey" || tp.name == "listValue">
				${tp.name}="${tp.value}"
				</#if>
			</#list>
				required="true"
			<#if f.editTag.hasParamStartsWith("_")>
			>
				<#list f.editTag.paramList as tp>
					<#if tp.name?starts_with('_')>
				${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
					</#if>
				</#list>
			${s}/@${f.editTag.name}>
			<#else>
			/>
			</#if>
		<#else>
			${s}@${f.editTag.name}
			<#if f.editTag.cssClass??>
				cssClass="${f.editTag.cssClass}"
			</#if>
				key="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
			<#if f.editTag.name?ends_with(".file")>
				value=""
			</#if>
			<#if (f.required!false)>
				required="true"
			</#if>
			<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			</#if></#list>
			<#if f.editTag.hasParamStartsWithAny("+_")>
			>
				<#list f.editTag.paramList as tp>
					<#assign tab = ""/>
					<#if f.editTag.name?ends_with(".uploader") && tp.name == '+defaultLink'>
						<#assign tab = "\t"/>
						<#if f.actionField>
				${s}#if ${f.name}?? && ${f.name}.exist>
						<#else>
				${s}#if ${actionDataFieldName}?? && ${actionDataFieldName}.${f.name}?? && ${actionDataFieldName}.${f.name}.exist>
						</#if>
					</#if>
					<#if tp.name?starts_with('+')>
				${tab}${s}@s.param name="${tp.name?substring(1)}"><@aurl au=tp.values/>${s}/@s.param>
					<#elseif tp.name?starts_with('_')>
				${tab}${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
					</#if>
					<#if tab?has_content>
				${s}/#if>
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
</#list>
