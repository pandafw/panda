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
		<#assign a = gen.stripStartMark(t)/>
		<#if t == '@refresh'>
			<li>${s}@n.a icon="icon-refresh" href="javascript:location.reload(true)">${s}@n.text name='button-refresh'/>${s}/@n.a></li>
		<#elseif t?starts_with('@')>
			<#assign a = a?split(':')/>
			<#if a[0]?has_content>
				<#assign ac = gen.getActionContext(a[0])/>
				<#assign an = gen.getActionName(a[0])/>
				<#assign ap = gen.getActionParam(a[0])/>
			<li>${s}@n.a icon="icon-${an}-${a[1]}"<#if t?contains('^')> target="_blank"</#if> action="${an}"<#if ac?has_content> namespace="${ac}"</#if>><#rt/>
				<#if ap?has_content>${s}@s.param name="${ap}">${s}@n.url action='${action.name}_${a[1]}' forceAddSchemeHostAndPort='true' escapeAmp='false'>
					<#list model.primaryKeyList as p>
						${s}@s.param name="${actionDataFieldName}.${p.name}" value="${actionDataFieldName}.${p.name}"/>
					</#list>
				${s}/@n.url>${s}/@s.param></#if>${s}@n.text name='button-${an}-${a[1]}'/>${s}/@n.a></li>
			</#if>
		<#else>
			<#assign ac = ''/>
			<#assign an = ''/>
			<#assign j = a?index_of(':')/>
			<#if j &gt; 0>
				<#assign a2 = a?substring(j + 1)/>
				<#if a2?starts_with('/')>
					<#assign ac = gen.getActionContext(a2)/>
					<#assign an = gen.getActionName(a2)/>
				<#else>
					<#assign a2 = action.name + '_' + a2/>
					<#assign an = a2/>
				</#if>
				<#assign a = a?substring(0, j)/>
			<#else>
				<#assign a2 = action.name + '_' + a/>
				<#assign an = a2/>
			</#if>
		${s}#if <#if t?contains('%')>action.hasDataPermission(${actionDataFieldName}!, "${a2}")<#else>action.hasPermission("${a2}")</#if>>
			<li>${s}@n.a icon="icon-${a}"<#if t?contains('^')> target="_blank"</#if> action="${an}"<#if ac?has_content> namespace="${ac}"</#if>><#rt/>
<#if t?contains('%')><#list model.primaryKeyList as p>
${s}@s.param name="${actionDataFieldName}.${p.name}" value="${actionDataFieldName}.${p.name}"/><#rt/>
</#list></#if>${s}@n.text name='button-${a}'/>${s}/@n.a></li>
		${s}/#if>
		</#if>
