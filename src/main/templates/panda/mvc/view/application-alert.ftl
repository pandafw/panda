<#if ApplicationErrors?has_content
	|| ApplicationWarnings?has_content
	|| ApplicationConfirms?has_content
	|| ApplicationMessages?has_content>
<div class="alert alert-dismissable<#rt/>
<#if ApplicationErrors?has_content>
 alert-danger<#rt/>
<#elseif ApplicationWarnings?has_content>
 alert-warning<#rt/>
<#elseif ApplicationConfirms?has_content>
 alert-info<#rt/>
<#elseif ApplicationMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<#if ApplicationErrors?has_content>
	<ul class="fa-ul p-application-errors">
	<#list ApplicationErrors?keys as k>
		<li class="p-application-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i>${ApplicationErrors[k]}</li>
	</#list>
	</ul>
</#if>
<#if ApplicationWarnings?has_content>
	<ul class="fa-ul p-application-warnings">
	<#list ApplicationWarnings?keys as k>
		<li class="p-application-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i>${ApplicationWarnings[k]}</li>
	</#list>
	</ul>
</#if>
<#if ApplicationConfirms?has_content>
	<ul class="fa-ul p-application-confirms">
	<#list ApplicationConfirms?keys as k>
		<li class="p-application-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i>${ApplicationConfirms[k]}</li>
	</#list>
	</ul>
</#if>
<#if ApplicationMessages?has_content>
	<ul class="fa-ul p-application-messages">
	<#list ApplicationMessages?keys as k>
		<li class="p-application-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i>${ApplicationMessages[k]}</li>
	</#list>
	</ul>
</#if>
</div>
</#if>
