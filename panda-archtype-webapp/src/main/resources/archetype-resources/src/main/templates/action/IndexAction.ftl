<html>
<head>
	<title><@p.text name="site-desc"/></title>
</head>

<body>
	<div class="p-well p-tcenter" style="margin: 0 10px 20px 10px">
		<@p.text name="site-desc"/>
	</div>

<#macro mitem ac ic tx>
	<#if a.canAccess(ac)>
		<@p.a cssClass="p-menu-item" action=ac>
			<i class="fa-4x ${a.getText(ic)}"></i>
			<div><@p.text name=tx/></div>
		</@p.a>
	</#if>
</#macro>

	<div class="p-menu">
		<@mitem ac="/pet/list"          ic="icon-pets-pet"          tx="navi-pets-pet"/>
		<div class="clearfix"></div>

		<hr/>
		<@mitem ac="/pages"       ic="icon-pages"                  tx="navi-pages"/>
		<@mitem ac="/media"       ic="icon-media"                  tx="navi-media"/>
		<@mitem ac="/tags"        ic="icon-tags"                   tx="navi-tags"/>
		<@mitem ac="/pdf"         ic="icon-super-html2pdf"         tx="navi-super-html2pdf"/>
		<div class="clearfix"></div>

<#if assist.loginUser??>
		<hr/>
		<@mitem ac="/user/profile/input"    ic="icon-user-profile"         tx="navi-user-profile"/>
		<@mitem ac="/user/password/change/" ic="icon-user-password-change" tx="navi-user-password-change"/>
		<div class="clearfix"></div>
</#if>

<#if assist.hasAdminRole()>
		<hr/>
		<#include "/panda/gems/admin/admin-menunav.ftl" />
		<div class="clearfix"></div>
</#if>

<#if assist.hasSuperRole()>
		<hr/>
		<#include "/panda/gems/admin/super-menunav.ftl" />
		<div class="clearfix"></div>
</#if>

		<br/>
		<br/>
	</div>
</body>
</html>

