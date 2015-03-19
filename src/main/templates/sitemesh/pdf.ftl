<@p.head contentType="text/html" charset="UTF-8" expiry="0"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "head.ftl"/>
	${head}
</head>

<body class="${assist.browser} lang-${assist.locale}">
	<div id="print">
		<div id="page_header">${title} | <@p.text name="site-name"/></div>
		<div id="page_footer"><span id="page_number"/> / <span id="page_count"/></div>
		${body}
	</div>

	<#include "foot.ftl"/>
</body>
</html>
