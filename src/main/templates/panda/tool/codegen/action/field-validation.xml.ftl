<#list ui.displayFieldList as f>
<#assign p = ""/>
<#assign required = false/>
<#if f.actionField>
	<#list action.propertyList as ap>
		<#if ap.name == f.name>
			<#assign p = ap/>
			<#break/>
		</#if>
	</#list>
<#else>
	<#list entity.propertyList as mp>
		<#if mp.name == f.name>
			<#assign p = mp/>
			<#if requiredPK && p.primaryKey!false>
				<#assign required = true/>
			</#if>
			<#break/>
		</#if>
	</#list>
</#if>
<#if f.required!false>
	<#assign required = true/>
</#if>
<#if required || f.validatorList?has_content>
	<#if p == "">${action.error("Can not find property [" + f.name + "] of entity [" + entity.name + "]")}</#if>
	<field name="<#if !f.actionField>${actionDataFieldName}.</#if>${f.name}">
</#if>
<#if required>
		<field-validator type="required<#if p.simpleJavaType == 'String'>string<#elseif p.simpleJavaType?starts_with('Upload')>file</#if>">
			<message key="validation-required<#if p.simpleJavaType == 'String'>string<#elseif p.simpleJavaType?starts_with('Upload')>file</#if>"/>
		</field-validator>
	<#if p.simpleJavaType == 'UploadImage'>
		<field-validator type="image">
			<message key="validation-image"/>
		</field-validator>
	</#if>
</#if>
<#if f.validatorList?has_content>
	<#list f.validatorList as v>
		<field-validator type="${v.type}">
		<#if v.paramList?has_content><#list v.paramList as vp>
			<param name="${vp.name?html}">${vp.value?html}</param>
		</#list></#if>
			<message key="${v.message?html}"/>
		</field-validator>
	</#list>
</#if>
<#if required || f.validatorList?has_content>
	</field>
</#if>
</#list>
