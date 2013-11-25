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
		<script type="text/javascript"><!--
		<#list ui.displayFieldList as f>
			<#assign _popup = {}/>
			<#if f.editTag??>
				<#list f.editTag.paramList as p>
					<#if p.name == "~popup">
						<#assign _popup = p.values/>
						<#break/>
					</#if>
				</#list>
			</#if>
			<#if _popup?has_content>
			function ${action.name}_${f.name}_onPopupCallback(sd) {
				<#if _popup.fields?has_content>
				<#list _popup.fields?keys as fk>
					<#list ui.fieldList as f2><#if f2.name == fk>
						<#if f2.editTag?? && f2.editTag.name == "n.viewfield">
				nuts.viewfield("#${action.name}<#if f2.modelField>_${actionDataFieldName}</#if>_${fk}").val(sd.${_popup.fields[fk]});
						<#else>
				$("#${action.name}<#if f2.modelField>_${actionDataFieldName}</#if>_${fk}").val(sd.${_popup.fields[fk]});
						</#if>
						<#break/>
					</#if></#list>
				</#list>
				</#if>
				$.popup().hide();
			}
		
			$(function() {
			<#assign pid = ""/>
			<#if _popup.ref?has_content>
				<#assign f2 = ui.getFieldByName(_popup.ref)/>
				<#assign pid = "popup_" + action.name + f2.modelField?string("_" + actionDataFieldName, "") + "_" + f2.name/>
			<#else>
				<#assign pid = "popup_" + action.name + f.modelField?string("_" + actionDataFieldName, "") + "_" + f.name/>
				$.popup({
					id: "${pid}",
					url: "${s}@n.url<#if _popup.namespace??> namespace="${_popup.namespace}"</#if> action="${_popup.action}" escapeAmp="false"><#rt>
				<#if _popup.params?has_content>
					<#list _popup.params?keys as pn>
${s}@s.param name="${pn}" value="${_popup.params[pn]}"/><#rt>
					</#list>
				</#if>
${s}/@n.url>"
				});
				
			</#if>
				$('#${action.name}<#if f.modelField>_${actionDataFieldName}</#if>_${f.name}').next().popup({
					id: "${pid}",
					target: "#${action.name}<#if f.modelField>_${actionDataFieldName}</#if>_${f.name}",
					callback: ${action.name}_${f.name}_onPopupCallback
				});
			});
			
			</#if>
		</#list>
		--></script>
