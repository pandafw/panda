<html>
<head>
	<title><@p.text name="title"/></title>
</head>

<body>

<div class="p-section" id="a_dataimp">
	<div class="p-header">
		<h3><@p.i icon="icon"/> <@p.text name="title"/></h3>
	</div>
	<#include "/action-alert.ftl"/>

	<@p.form id="dataimp" method="post" enctype="multipart/form-data" theme="bs3h">
		<@p.select key="target" list="%{action.targets}"/>
		<@p.file
			key="file"
			value=""
			size="50"
			onchange="onFileChange(this)"
		/>
		<@p.checkbox name="deleteAll" fieldLabel="#(deleteAll)" fieldValue="true"/>
		<@p.div cssClass="p-buttons">
			<@p.submit icon="fa fa-inbox" label="#(btn-import)"/>
		</@p.div>
	</@p.form>
	
	<script type="text/javascript">
		function onFileChange(e) {
			var fn = $(e).val().toUpperCase();
			var st = $('#dataimp_target').get(0);
			for (var i = 0; i < st.options.length; i++) {
				if (fn.indexOf(st.options[i].text) >= 0) {
					st.selectedIndex = i;
					break;
				}
			}
		}
	</script>

<#if result?has_content>
	<hr/>
	<#list result as t>
	<div class="panel panel-success">
		<div class="panel-heading">&lt;&lt;${t[0]}&gt;&gt; (${(t?size - 2)?c})</div>
		<div class="table-responsive">
			<table class="table table-striped table-bordered p-fz80p p-th-nowrap p-td-nowrap">
			<thead>
				<tr><th>##</th><#list t[1] as h><th>${assist.escapePhtml(h!"")}</th></#list></tr>
			</thead>
			<tbody>
			<#list t as r><#if r_index gt 1>
				<tr><th>${(r_index - 1)?c}</th><#list r as v><td>${assist.escapePhtml(v!"")}</td></#list></tr>
			</#if></#list>
			<tbody>
			</table>
		</div>
	</div>
	<br/>
	</#list>
</#if>
</div>

</body>
</html>
