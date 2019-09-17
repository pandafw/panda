<#macro navi ap ac ic tx>
	<#if a.canAccess(ac)>
		<li<#if path?starts_with(ap)> class="active"</#if>><@p.a action=ac><i class="${a.getText(ic)}"></i> <span><@p.text name=tx/></span></@p.a></li>
	</#if>
</#macro>

<div id="navi_pets" class="panel panel-info side-navi">
	<div class="panel-heading">
		<i class="fa fa-qq"></i> <span><@p.text name="navi-pets"/></span>
	</div>
	<div class="panel-body">
		<ul class="nav nav-stacked">
			<@navi ap="/pet/"         ac="/pet/list"          ic="icon-pets-pet"               tx="navi-pets-pet"/>
		</ul>
	</div>
</div>

<#if assist.loginUser??>
<div id="navi_user" class="panel panel-primary side-navi">
	<div class="panel-heading">
		<i class="fa fa-user"></i> <span><@p.text name="navi-user-account"/></span>
	</div>
	<div class="panel-body">
		<ul class="nav nav-stacked">
			<@navi ap="/user/profile/"         ac="/user/profile/input"    ic="icon-user-profile"         tx="navi-user-profile"/>
			<@navi ap="/user/password/change/" ac="/user/password/change/" ic="icon-user-password-change" tx="navi-user-password-change"/>
		</ul>
	</div>
</div><!-- end of navi_user -->
</#if>

<#if assist.hasAdminRole()>
	<#include "/panda/gems/admin/admin-sidenav.ftl">
</#if>

<#if assist.hasSuperRole()>
	<#include "/panda/gems/admin/super-sidenav.ftl">
</#if>
