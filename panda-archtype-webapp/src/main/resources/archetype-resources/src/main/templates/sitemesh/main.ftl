<@p.head contentType="text/html" charset="UTF-8"/>
<!DOCTYPE html>
<html lang="${locale}">
<head>
	<#include "head.ftl"/>
	${head}
</head>

<body class="${assist.browser} lang-${locale}">
<div id="main">
	<#include "main-header.ftl"/>

	<#include "main-body.ftl"/>

	<#include "main-footer.ftl"/>
</div>

	<#include "foot.ftl"/>
</body>
</html>
