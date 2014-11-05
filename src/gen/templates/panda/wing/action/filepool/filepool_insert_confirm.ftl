<html>
<head>
	<title><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<ul class="p-toolbar">
	<#if action.hasPermission("filepool_list")>
		<li><@p.a icon="icon-list" action="filepool_list"><@p.text name='button-list'/></@p.a></li>
	</#if>
	</ul>


	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form cssClass="p-cform" id="filepool" initfocus="true" method="post" theme="bs3h">
			<@n.viewfield
				key="d.id"
			/>
			<@p.viewfield
				key="d.name"
			/>
			<@p.viewfield
				key="d.size"
			/>
			<@p.viewfield
				key="d.date"
				format="timestamp"
			/>
			<@p.viewfield
				key="d.flag"
				format="check"
			/>
			<@p.viewfield
				key="d.readable"
				format="check"
			/>
			<@p.viewfield
				key="d.writeable"
				format="check"
			/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-insert-execute" action="filepool_insert_execute"><@p.text name="button-insert-execute"/></@p.submit>
			<@p.submit icon="icon-back" action="filepool_insert_input"><@p.text name="button-back"/></@p.submit>
		</@p.div>
	</@p.form>
</div>

</body>
</html>
