<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Ошибка"/>
<%@ include file="_header.jspf" %>

<h2>Ошибка</h2>
<p class="error">${error}</p>

<%@ include file="_footer.jspf" %>
