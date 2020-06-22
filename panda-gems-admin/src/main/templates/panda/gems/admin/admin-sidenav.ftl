<#macro anavi ap ac ic tx>
	<#if a.canAccess(ac)>
		<li class="nav-item<#if path?starts_with(ap)> active</#if>"><@p.a cssClass="nav-link" action=ac><i class="${a.getText(ic)}"></i> <span><@p.text name=tx/></span></@p.a></li>
	</#if>
</#macro>
<div id="navi_admin" class="panel panel-warning card side-navi">
	<div class="panel-heading card-header bg-warning text-dark">
		<i class="fa fa-gear"></i> <span><@p.text name="navi-admin-site"/></span>
	</div>
	<div class="panel-body card-body">
		<ul class="nav nav-stacked flex-column">
			<@anavi ap="/admin/users/"          ac="/admin/users/list"      ic="icon-admin-users"          tx="navi-admin-users"/>
			<@anavi ap="/admin/pages/"          ac="/admin/pages/list"      ic="icon-admin-pages"          tx="navi-admin-pages"/>
			<@anavi ap="/admin/media/"          ac="/admin/media/list"      ic="icon-admin-media"          tx="navi-admin-media"/>
			<@anavi ap="/admin/files/"          ac="/admin/files/list"      ic="icon-admin-files"          tx="navi-admin-files"/>
			<@anavi ap="/admin/tags/"           ac="/admin/tags/list"       ic="icon-admin-tags"           tx="navi-admin-tags"/>
		</ul>
	</div>
</div><!-- end of navi_admin -->
