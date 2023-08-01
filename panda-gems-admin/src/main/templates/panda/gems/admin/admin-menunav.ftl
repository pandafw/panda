<#macro aitem ac ic tx>
	<#if a.canAccess(ac)>
		<@p.a cssClass="ui-menu-item" action=ac>
			<i class="fa-4x ${a.getText(ic)}"></i>
			<div><@p.text name=tx/></div>
		</@p.a>
	</#if>
</#macro>
		<@aitem ac="/admin/users/list"      ic="icon-admin-users"          tx="navi-admin-users"/>
		<@aitem ac="/admin/pages/list"      ic="icon-admin-pages"          tx="navi-admin-pages"/>
		<@aitem ac="/admin/media/list"      ic="icon-admin-media"          tx="navi-admin-media"/>
		<@aitem ac="/admin/files/list"      ic="icon-admin-files"          tx="navi-admin-files"/>
		<@aitem ac="/admin/tags/list"       ic="icon-admin-tags"           tx="navi-admin-tags"/>
