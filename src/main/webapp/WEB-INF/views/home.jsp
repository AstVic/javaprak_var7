<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Главная"/>
<%@ include file="_header.jspf" %>

<h2>Главная</h2>
<p>Web-приложение для работы с резюме, вакансиями и откликами.</p>

<p>
    <a class="button" href="<c:url value='/resumes'/>">Размещение резюме</a>
    <a class="button" href="<c:url value='/search'/>">Подбор вакансий</a>
</p>

<%@ include file="_footer.jspf" %>
