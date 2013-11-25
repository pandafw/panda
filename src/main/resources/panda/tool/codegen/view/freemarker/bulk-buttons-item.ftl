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
		<#assign a0 = gen.stripStart(t, "%*^")/>
		<#if a0?starts_with('@')>
			<#assign a = a0?substring(1)/>
			<#assign j = a?index_of(':')/>
			<#if j &gt; 0>
				<#assign a2 = action.name + '_' + a?substring(j + 1)/>
				<#assign a = a?substring(0, j)/>
			<#else>
				<#assign a2 = action.name + '_' + a/>
			</#if>
		${s}#if action.hasPermission("${a2}")>
			${s}@n.submit icon="icon-${a}" action="${a2}" theme="simple">${s}@n.text name='button-${a}'/>${s}/@n.submit>
		${s}/#if>
		<#else>
			<#assign a = a0/>
			<#assign j = a?index_of(':')/>
			<#if j &gt; 0>
				<#assign a2 = action.name + '_' + a?substring(j + 1)/>
				<#assign a = a?substring(0, j)/>
			<#else>
				<#assign a2 = action.name + '_' + a/>
			</#if>
		${s}#if action.hasPermission("${a2}")>
			${s}@n.url var="_u_" action='${a2}'/>
		<#if t?contains('^')>
			${s}@n.submit icon="icon-${a}" onclick="window.open('${d}{_u_}');return false;" theme="simple">${s}@n.text name='button-${a}'/>${s}/@n.submit>
		<#else>
			${s}@n.submit icon="icon-${a}" onclick="location.href='${d}{_u_}';return false;" theme="simple">${s}@n.text name='button-${a}'/>${s}/@n.submit>
		</#if>
		${s}/#if>
		</#if>
