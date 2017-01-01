<@p.head contentType="text/html" charset="UTF-8" maxAge="0"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
