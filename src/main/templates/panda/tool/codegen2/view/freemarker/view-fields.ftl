<#list ui.displayFieldList as f>
	<#if f.before?has_content>
			${f.before}
	</#if>
	<#assign tag = ""/>
	<#if f.viewTag??>
		<#assign tag = f.viewTag/>
	<#elseif f.editTag??>
		<#assign tag = f.editTag/>
	</#if>
	<#if f.content?has_content>
			${f.content}
	<#elseif tag?has_content>
		<#if tag.name?ends_with(".hidden")>
			${s}@${tag.name}
			<#if f.editTag.cssClass??>
				cssClass="${f.editTag.cssClass}"
			</#if>
				name="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
			<#list tag.paramList as tp><#if gen.startsWithLetter(tp.name) && !(tp.name != "readonly")>
				${tp.name}="${tp.value}"
			</#if></#list>
			/>
		<#elseif tag.name?ends_with(".uploader")>
			${s}@${tag.name}
			<#if tag.cssClass??>
				cssClass="${tag.cssClass}"
			</#if>
				key="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
				readonly="true"
			<#list tag.paramList as tp><#if gen.startsWithLetter(tp.name) && !(tp.name != "readonly")>
				${tp.name}="${tp.value}"
			</#if></#list>
			<#if f.editTag.hasParamStartsWithAny("+_")>
			>
				<#assign tab = ""/>
				<#if ui.templates?seq_contains("update") 
					|| ui.templates?seq_contains("view")
					|| ui.templates?seq_contains("print")>
					<#assign tab = "\t"/>
					<#if f.actionField>
				${s}#if ${f.name}?? && ${f.name}.exist>
					<#else>
				${s}#if ${actionDataFieldName}?? && ${actionDataFieldName}.${f.name}?? && ${actionDataFieldName}.${f.name}.exist>
					</#if>
				</#if>
				<#list f.editTag.paramList as tp>
					<#if (ui.templates?seq_contains("insert") || ui.templates?seq_contains("copy")) 
						&& (tp.name == '+defaultLink' || tp.name == '_defaultText')>
					<#elseif tp.name?starts_with('+')>
				${tab}${s}@s.param name="${tp.name?substring(1)}"><@aurl au=tp.values/>${s}/@s.param>
					<#elseif tp.name?starts_with('_')>
				${tab}${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
					</#if>
				</#list>
				<#if tab?has_content>
				${s}/#if>
				</#if>
			${s}/@${f.editTag.name}>
			<#else>
			/>
			</#if>
		<#elseif f.viewTag?? || tag.name?ends_with(".viewfield")>
			${s}@${tag.name}
			<#if tag.cssClass??>
				cssClass="${tag.cssClass}"
			</#if>
				key="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
			<#list tag.paramList as tp><#if gen.startsWithLetter(tp.name)>
				${tp.name}="${tp.value}"
			</#if></#list>
			<#if tag.hasParamStartsWith("_")>
			>
				<#list tag.paramList as tp>
					<#if tp.name?starts_with('_')>
				${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
					</#if>
				</#list>
			${s}/@${tag.name}>
			<#else>
			/>
			</#if>
		<#else>
			<#assign _format = ""/>
			<#list tag.paramList as tp>
				<#if tp.name == "format">
					<#assign _format = tp.value/>
				</#if>
			</#list>
			${s}@p.viewfield
				key="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}"
			<#if _format?has_content>
				format="${_format}"
			<#elseif tag.name?ends_with(".checkbox")>
				format="check"
			<#elseif tag.name?ends_with(".datepicker")>
				format="date"
			<#elseif tag.name?ends_with(".timepicker")>
				format="time"
			<#elseif tag.name?ends_with(".datetimepicker")>
				format="datetime"
			</#if>
			<#list tag.paramList as tp>
				<#if tp.name == "list" || tp.name == "listKey" || tp.name == "listValue">
				${tp.name}="${tp.value}"
				</#if>
			</#list>
			<#if tag.hasParamStartsWith("_")>
			>
				<#list tag.paramList as tp>
					<#if tp.name?starts_with('_')>
				${s}@s.param name="${tp.name?substring(1)}">${tp.value}${s}/@s.param>
					</#if>
				</#list>
			${s}/@p.viewfield>
			<#else>
			/>
			</#if>
		</#if>
	</#if>
	<#if f.after?has_content>
			${f.after}
	</#if>
</#list>