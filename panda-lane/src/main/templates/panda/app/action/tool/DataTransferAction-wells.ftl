<#if a.dataSettings?has_content>
	<div class="panel panel-info">
		<div class="panel-heading"><a class="btn btn-primary" data-toggle="collapse" href="#d_settings">Data Settings</a></div>
		<div class="collapse" id="d_settings">
		<table class="table table-striped">
		<tbody>
	<#list a.dataSettings?keys?sort as k>
		<tr><td>${k?html}</td><td>${a.dataSettings[k]?html}</td></tr>
	</#list>
		</tbody>
		</table>
		</div>
	</div>
</#if>
