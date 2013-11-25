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
<#if "true" == props["ui.input.step"]!"">
	${s}#if action.getTextAsBoolean('ui-input-step', false)>
		<ul class="n-step">
			<li class="n-step-first">${s}@n.text name="step-${ui.name}_input"/></li>
			<li class="n-step-arrow">${s}@n.text name="step-arrow-e">&gt;${s}/@n.text></li>
			<li class="n-step-next">${s}@n.text name="step-${ui.name}_confirm"/></li>
			<li class="n-step-arrow">${s}@n.text name="step-arrow-e">&gt;${s}/@n.text></li>
			<li class="n-step-last n-step-self">${s}@n.text name="step-${ui.name}Success"/></li>
		</ul>
		<div class="n-clear"><hr/></div>
	${s}/#if>
</#if>

		${s}#include "/nuts/exts/struts2/views/action-prompt.ftl"/>

		${s}@n.form cssClass="n-sform" id="<#if ui.formId?has_content>${ui.formId}<#else>${action.name}</#if>" method="post"<#if ui.theme?has_content> theme="${ui.theme}"</#if>>
			<#include "view-fields.ftl"/>
			<#include "input-success-actions.ftl"/>
		${s}/@n.form>
	</div>
</div>

<@footer/>
