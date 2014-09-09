<#if actionErrors?has_content
	|| actionWarnings?has_content
	|| actionConfirms?has_content
	|| actionMessages?has_content
	|| fieldErrors?has_content>
<div class="alert alert-dismissable<#rt/>
<#if actionErrors?has_content || fieldErrors?has_content>
 alert-danger<#rt/>
<#elseif actionWarnings?has_content>
 alert-warning<#rt/>
<#elseif actionConfirms?has_content>
 alert-info<#rt/>
<#elseif actionMessages?has_content>
 alert-success<#rt/>
</#if>
">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
	<@p.actionerror/>
	<#if fieldErrors?has_content>
		<@p.fielderror label="true"/>
	</#if>
	<@p.actionwarning/>
	<@p.actionmessage/>
	<@p.actionconfirm/>
</div>
</#if>