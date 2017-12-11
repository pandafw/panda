<@p.head contentType="text/html" charset="UTF-8" maxAge="0"/>
<!DOCTYPE html>
<html lang="${locale}">
<head>
	<#include "head.ftl"/>
	${head}
</head>

<body class="print ${assist.browser} lang-${assist.locale}">
	<div id="print">
		${body}
	</div>

	<#include "foot.ftl"/>
</body>
</html>
