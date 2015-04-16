<#if SessionErrors?has_content
	|| SessionWarnings?has_content
	|| SessionConfirms?has_content
	|| SessionMessages?has_content>
<div class="alert alert-dismissable<#rt/>
<#if SessionErrors?has_content>
 alert-danger<#rt/>
<#elseif SessionWarnings?has_content>
 alert-warning<#rt/>
<#elseif SessionConfirms?has_content>
 alert-info<#rt/>
<#elseif SessionMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<#if SessionErrors?has_content>
	<ul class="fa-ul p-session-errors">
	<#list SessionErrors?keys as k>
		<li class="p-session-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i>${SessionErrors[k]}</li>
	</#list>
	</ul>
</#if>
<#if SessionWarnings?has_content>
	<ul class="fa-ul p-session-warnings">
	<#list SessionWarnings?keys as k>
		<li class="p-session-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i>${SessionWarnings[k]}</li>
	</#list>
	</ul>
</#if>
<#if SessionConfirms?has_content>
	<ul class="fa-ul p-session-confirms">
	<#list SessionConfirms?keys as k>
		<li class="p-session-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i>${SessionConfirms[k]}</li>
	</#list>
	</ul>
</#if>
<#if SessionMessages?has_content>
	<ul class="fa-ul p-session-messages">
	<#list SessionMessages?keys as k>
		<li class="p-session-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i>${SessionMessages[k]}</li>
	</#list>
	</ul>
</#if>
</div>
</#if>
