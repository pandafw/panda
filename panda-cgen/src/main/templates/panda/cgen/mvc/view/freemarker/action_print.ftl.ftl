<#include "common.ftl"/>
<@header/>
<@headinc step=""/>

<div class="p-section">
	<@sheader steps=[ ui.name ]/>
	<@swell/>

	<div id="${formId}_alert">${s}#include "/action-alert.ftl"/></div>

${s}#if r??>
	${s}@p.form cssClass="p-vform" id="${formId}"${gen.focusme(ui)}>
		<#include "edit-view-fields.ftl"/>
	${s}/@p.form>
${s}/#if>
</div>

<@footinc step=""/>
<@footer/>
