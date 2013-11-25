<#--
/*
 * This file is part of Nuts Framework.
 * Copyright(C) 2009-2012 Nuts Develop Team.
 *
 * Nuts Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License any later version.
 *
 * Nuts Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	 See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuts Framework. If not, see <http://www.gnu.org/licenses/>.
 */
-->
<#list ui.displayFieldList as f><#if f.modelField>
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
