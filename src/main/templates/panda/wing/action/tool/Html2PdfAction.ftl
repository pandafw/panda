<html>
<head>
	<title><@p.text name="title"/></title>
</head>
<body>

<div class="p-section a-html2pdf">
	<div class="p-header">
		<h3><i class="fa fa-file-text"></i> <@p.text name="title"/></h3>
	</div>

	<@p.form id="html2pdf" method="get" target="_blank" theme="bs3" loadmask="false">
		<@p.select key="charset" list="consts.charsets" emptyOption="true"/>
		<@p.textarea key="url" rows="5">
			<@s.param name="after">(Ctrl+Enter to generate PDF)<br/></@s.param>
		</@p.textarea>
		<@p.submit icon="icon-pdf"><@p.text name="button-pdf"/></@p.submit>
	</@p.form>
	
	<#include "/panda/exts/struts2/views/action-alert.ftl"/>
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
