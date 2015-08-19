<html>
<head>
	<title><@p.text name="title-print"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.text name="title-${actionResult}"><@s.param><@p.text name="title"/></@s.param></@p.text></h3>
	</div>

	<#include "/panda/exts/struts2/views/action-alert.ftl"/>

	<@p.form cssClass="p-vform" id="filepool" theme="bs3h">
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
	</@p.form>

</div>

</body>
</html>
