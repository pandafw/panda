<div id="footer" class="navbar navbar-default">
	<div class="container">
		<!--div id="footer_navi"></div-->
		<div id="footer_site">
			<div id="site_cpr">&copy; ${assist.systemYear?c} ${request.serverName!}</div>
			<div id="site_ver">Version: ${assist.appVersion} Cost: ${assist.requestElapsedTime}</div>
		</div>
	</div>
	<div totop="true" class="ui-corner ui-corner-br" style="display:none"><i class="fa fa-angle-up"></i></div>
<#if appDebug>
	<div class="p-debug ui-corner ui-corner-bl" data-toggle="modal" data-target="#a_debug_modal"><i class="fa fa-bug"></i></div>
	<#include "/panda/mvc/view/debug-modal.ftl" />
</#if>
</div>

