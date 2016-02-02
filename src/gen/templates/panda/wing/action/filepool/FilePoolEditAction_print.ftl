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
	<#if a.displayField("id")>
			<@n.viewfield
				key="id"
				value="%{r.id}"
			/>
	</#if>
	<#if a.displayField("name")>
			<@p.viewfield
				key="name"
				value="%{r.name}"
			/>
	</#if>
	<#if a.displayField("size")>
			<@p.viewfield
				key="size"
				value="%{r.size}"
			/>
	</#if>
	<#if a.displayField("date")>
			<@p.viewfield
				key="date"
				value="%{r.date}"
				format="timestamp"
			/>
	</#if>
	<#if a.displayField("flag")>
			<@p.viewfield
				key="flag"
				value="%{r.flag}"
			/>
	</#if>
	</@p.form>
</#if>
</div>

</body>
</html>
