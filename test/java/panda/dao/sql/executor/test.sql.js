<%@
	import java.util.List;
%>
<%!
	private boolean isNotEmpty(List list) {
		return list != null && !list.isEmpty();
	}
%>
SELECT * FROM TEST 
WHERE 
<% if ((Integer)#id > 0) { %> ID=:id <% } %>
<% if (isNotEmpty((List)#idList)) { %> AND ID IN (:idList) <% } %>
<% if (#name != null) { append(" AND NAME=:name "); } %>
ORDER BY <%= #orderCol == null ? "ID" : #orderCol %> <%= #orderDir == null ? "" : #orderDir %>
