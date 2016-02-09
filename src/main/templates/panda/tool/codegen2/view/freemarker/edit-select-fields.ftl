<#list ui.displayFieldList as f><#if !f.actionField>
	<#assign p = {}/>
	<#list entity.primaryKeyList as mp>
		<#if mp.name == f.name>
			<#assign p = mp/>
			<#break/>
		</#if>
	</#list>
	${s}#if a.displayField("${f.name}")>
	<#if p?has_content>
		<#if f.before?has_content>
			${f.before}
		</#if>
		<#if f.editTag?? && (f.editTag.name?ends_with(".hidden") || f.editTag.name?ends_with(".viewfield"))>
			${s}@p.textfield
				name="<#if f.actionField>a.</#if>${f.name}"
				value="%{<#if f.actionField>a<#else>r</#if>.${f.name}}"
				required="true"
				label="%{getText('${ui.name}-${f.name}', '')}"
				tooltip="%{getText('${ui.name}-${f.name}-tip', '')}"
			<#if f.editTag.hasParamStartsWith("_")>
			<#list f.editTag.paramList as tp>
				<#if tp.name?starts_with('_')>
				${tp.name?substring(1)}="${tp.value}"
				</#if>
			</#list>
			</#if>
			/>
		<#elseif f.editTag??>
			${s}@${f.editTag.name}
			<#if f.editTag.cssClass??>
				cssClass="${f.editTag.cssClass}"
			</#if>
				name="<#if f.actionField>a.</#if>${f.name}"
				value="%{<#if f.actionField>a<#else>r</#if>.${f.name}}"
			<#list f.editTag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			</#if></#list>
				label="%{getText('${ui.name}-${f.name}', '')}"
<#if !(f.editTag.name?ends_with(".viewfield"))>
				tooltip="%{getText('${ui.name}-${f.name}-tip', '')}"
</#if>
			/>
		</#if>
		<#if f.after?has_content>
			${f.after}
		</#if>
	</#if>
	${s}/#if>
</#if></#list>
