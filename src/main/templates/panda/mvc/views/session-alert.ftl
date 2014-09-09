<#if Session??>
<#if Session.errors?has_content
	|| Session.warnings?has_content
	|| Session.confirms?has_content
	|| Session.messages?has_content>
<div class="alert alert-dismissable<#rt/>
<#if Session.errors?has_content>
 alert-danger<#rt/>
<#elseif Session.warnings?has_content>
 alert-warning<#rt/>
<#elseif Session.confirms?has_content>
 alert-info<#rt/>
<#elseif Session.messages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
<#if Session.errors?has_content>
	<ul class="fa-ul p-session-errors">
	<#list Session.errors?keys as k>
		<li class="p-session-error" msgId="${k}"><i class="fa-li fa fa-exclamation-circle"></i>${Session.errors[k]}</li>
	</#list>
	</ul>
</#if>
<#if Session.warnings?has_content>
	<ul class="fa-ul p-session-warnings">
	<#list Session.warnings?keys as k>
		<li class="p-session-warning" msgId="${k}"><i class="fa-li fa fa-exclamation-triangle"></i>${Session.warnings[k]}</li>
	</#list>
	</ul>
</#if>
<#if Session.confirms?has_content>
	<ul class="fa-ul p-session-confirms">
	<#list Session.confirms?keys as k>
		<li class="p-session-confirm" msgId="${k}"><i class="fa-li fa fa-question-circle"></i>${Session.confirms[k]}</li>
	</#list>
	</ul>
</#if>
<#if Session.messages?has_content>
	<ul class="fa-ul p-session-messages">
	<#list Session.messages?keys as k>
		<li class="p-session-message" msgId="${k}"><i class="fa-li fa fa-info-circle"></i>${Session.messages[k]}</li>
	</#list>
	</ul>
</#if>
</div>
</#if>
</#if>