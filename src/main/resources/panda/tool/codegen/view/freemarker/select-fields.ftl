<#list ui.displayFieldList as f><#if !f.actionField>
	<#assign p = {}/>
	<#list model.primaryKeyList as mp>
		<#if mp.name == f.name>
			<#assign p = mp/>
			<#break/>
		</#if>
	</#list>
	<#if p?has_content>
		<#if f.before?has_content>
			${f.before}
		</#if>
		<#if f.editTag?? && (f.editTag.name?ends_with(".hidden") || f.editTag.name?ends_with(".viewfield"))>
			${s}@n.textfield
				name="${actionDataFieldName}.${f.name}"
				required="true"
				label="%{getText('${ui.name}-${f.name}', '')}"
				tooltip="%{getText('${ui.name}-${f.name}-tip', '')}"
			<#if f.editTag.hasParamStartsWith("_")>
			>
			<#list f.editTag.paramList as tp>
				<#if tp.name?starts_with('_')>
					${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
				</#if>
			</#list>
			${s}/@n.textfield>
			<#else>
			/>
			</#if>
		<#elseif f.editTag??>
			${s}@${f.editTag.name}
			<#if f.editTag.cssClass??>
				cssClass="${f.editTag.cssClass}"
			</#if>
				name="${actionDataFieldName}.${f.name}"
			<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			</#if></#list>
				label="%{getText('${ui.name}-${f.name}', '')}"
<#if !(f.editTag.name?ends_with(".viewfield"))>
				tooltip="%{getText('${ui.name}-${f.name}-tip', '')}"
</#if>
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
		</#if>
		<#if f.after?has_content>
			${f.after}
		</#if>
	</#if>
</#if></#list>
