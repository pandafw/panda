<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section a-html2pdf">
	<div class="p-header">
		<h3><i class="fa fa-file-text"></i> <@p.text name="title"/></h3>
	</div>

	<@p.form id="html2pdf" method="get" target="_blank" theme="bs3h" loadmask="false">
		<@p.select key="charset" list="%{consts.charsets}" emptyOption="true"/>
		<@p.textarea key="url" rows="5">
			<@p.param name="after">(Ctrl+Enter to generate PDF)<br/></@p.param>
		</@p.textarea>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="icon-pdf" label="#(button-pdf)"/>
		</@p.div>
	</@p.form>
	
	<#include "/action-alert.ftl"/>

	<script type="text/javascript">
		function onPageLoad() {
			$('#html2pdf_url').focus().keyup(function(evt) {
				if (evt.ctrlKey && evt.which == 13) {
					$('#html2pdf').submit();
				}
			});
		}
	</script>
</div>

</body>
</html>
