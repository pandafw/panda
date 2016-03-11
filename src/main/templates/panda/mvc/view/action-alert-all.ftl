<#if actionErrors?has_content
	|| actionWarnings?has_content
	|| actionConfirms?has_content
	|| actionMessages?has_content
	|| paramErrors?has_content>
<div class="p-alert alert alert-dismissable fade in<#rt/>
<#if actionErrors?has_content || paramErrors?has_content>
 alert-danger<#rt/>
<#elseif actionWarnings?has_content>
 alert-warning<#rt/>
<#elseif actionConfirms?has_content>
 alert-info<#rt/>
<#elseif actionMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<@p.actionerror/>
	<#if paramErrors?has_content>
		<@p.fielderror label="true"/>
	</#if>
	<@p.actionwarning/>
	<@p.actionmessage/>
	<@p.actionconfirm/>
</div>
</#if>