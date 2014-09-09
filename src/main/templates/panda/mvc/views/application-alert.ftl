<#if Application??>
<#if Application.errors?has_content
	|| Application.warnings?has_content
	|| Application.confirms?has_content
	|| Application.messages?has_content>
<div class="alert alert-dismissable<#rt/>
<#if Application.errors?has_content>
 alert-danger<#rt/>
<#elseif Application.warnings?has_content>
 alert-warning<#rt/>
<#elseif Application.confirms?has_content>
 alert-info<#rt/>
<#elseif Application.messages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<#if Application.errors?has_content>
	<ul class="fa-ul p-application-errors">
	<#list Application.errors?keys as k>
		<li class="p-application-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i>${Application.errors[k]}</li>
	</#list>
	</ul>
</#if>
<#if Application.warnings?has_content>
	<ul class="fa-ul p-application-warnings">
	<#list Application.warnings?keys as k>
		<li class="p-application-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i>${Application.warnings[k]}</li>
	</#list>
	</ul>
</#if>
<#if Application.confirms?has_content>
	<ul class="fa-ul p-application-confirms">
	<#list Application.confirms?keys as k>
		<li class="p-application-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i>${Application.confirms[k]}</li>
	</#list>
	</ul>
</#if>
<#if Application.messages?has_content>
	<ul class="fa-ul p-application-messages">
	<#list Application.messages?keys as k>
		<li class="p-application-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i>${Application.messages[k]}</li>
	</#list>
	</ul>
</#if>
</div>
</#if>
</#if>