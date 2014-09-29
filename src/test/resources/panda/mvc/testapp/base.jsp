<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="panda.mvc.ActionContext" %>
<% ActionContext ac = (ActionContext)request.getAttribute("panda.mvc.ActionContext");%>
<%=ac.getBase()%>