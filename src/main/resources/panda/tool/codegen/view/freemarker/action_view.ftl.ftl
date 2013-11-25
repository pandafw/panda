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
<#include "common.ftl"/>
<@header/>

<div class="n-sect">
	<div class="n-sect-head">
		<#include "success-toolbar.ftl"/>
		<h3 class="n-sect-title">${s}@n.text name="title-${d}{actionResult}">${s}@s.param>${s}@n.text name="title"/>${s}/@s.param>${s}/@n.text></h3>
	</div>
	<div class="n-sect-body">
		${s}#include "/nuts/exts/struts2/views/action-prompt.ftl"/>

		${s}@n.form cssClass="n-vform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
			<#include "view-fields.ftl"/>
			<#include "input-success-actions.ftl"/>
		${s}/@n.form>
	</div>
</div>

<@footer/>
