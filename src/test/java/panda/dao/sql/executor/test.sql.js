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
<% if (isNotEmpty((List)#intList)) { %> AND ID IN (:intList) <% } %>
<% if (#fstr != null) { append(" AND FSTR=:fstr "); } %>
ORDER BY <%= #orderCol == null ? "ID" : #orderCol %> <%= #orderDir == null ? "" : #orderDir %>
