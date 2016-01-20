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
	<@p.form cssClass="p-vform" id="property" theme="bs3h">
			<@p.viewfield
				key="id"
				value="%{r.id}"
			/>
			<@p.viewfield
				key="clazz"
				value="%{r.clazz}"
			/>
			<@p.viewfield
				key="language"
				value="%{r.language}"
				list="consts.localeLanguageMap"
			/>
			<@p.viewfield
				key="country"
				value="%{r.country}"
				list="consts.localeCountryMap"
			/>
			<@p.viewfield
				key="name"
				value="%{r.name}"
			/>
			<@p.viewfield
				key="value"
				value="%{r.value}"
			/>
			<@p.viewfield
				key="memo"
				value="%{r.memo}"
			/>
			<@p.viewfield
				key="status"
				value="%{r.status}"
				list="%{consts.dataStatusMap}"
			/>
			<@p.viewfield
				key="uusid"
				value="%{r.uusid}"
			/>
			<@p.viewfield
				key="uusnm"
				value="%{r.uusnm}"
			/>
			<@p.viewfield
				key="utime"
				value="%{r.utime}"
				format="datetime"
			/>
	</@p.form>
</#if>
</div>

</body>
</html>
