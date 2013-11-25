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
<#if ui.params.actions?has_content>
	<#assign _uas = ui.params.actions?split(' ')/>
	<#if _uas?has_content>
			${s}#assign _buttons_ = [] />
		<#list _uas as _ua>
			<#if _ua?starts_with('*')>
				<#assign _a = _ua?substring(1)/>
			<#else>
				<#assign _a = _ua/>
			</#if>
			<#include "input-actions-item.ftl"/>
		</#list>
			${s}#include "/nuts/exts/struts2/views/form-buttons.ftl"/>
			${s}@form_buttons buttons=_buttons_/>
	</#if>
</#if>
