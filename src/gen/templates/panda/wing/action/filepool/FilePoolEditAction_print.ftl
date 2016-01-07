<html>
<head>
	<title><@p.text name="title-print"><@p.param name="title" value="#(title)"/></@p.text></title>
</head>
<body>

<div class="p-section">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title-print"><@p.param name="title" value="#(title)"/></@p.text></h3>
	</div>

	<#include "/action-alert.ftl"/>

<#if r??>
	<@p.form cssClass="p-vform" id="filepool" theme="bs3h">
			<@n.viewfield
				key="id"
				value="%{r.id}"
			/>
			<@p.viewfield
				key="name"
				value="%{r.name}"
			/>
			<@p.viewfield
				key="size"
				value="%{r.size}"
			/>
			<@p.viewfield
				key="date"
				value="%{r.date}"
				format="timestamp"
			/>
			<@p.viewfield
				key="flag"
				value="%{r.flag}"
				format="check"
			/>
			<@p.viewfield
				key="readable"
				value="%{r.readable}"
				format="check"
			/>
			<@p.viewfield
				key="writeable"
				value="%{r.writeable}"
				format="check"
			/>
	</@p.form>
</#if>
</div>

</body>
</html>
