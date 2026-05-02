<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Размещение резюме"/>
<%@ include file="_header.jspf" %>

<h2>Размещение резюме</h2>
<p><a class="button" href="<c:url value='/resumes/new'/>">Создать резюме</a></p>

<table data-testid="resumes-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Соискатель</th>
        <th>Должность</th>
        <th>Желаемая зарплата</th>
        <th>Статус</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="resume" items="${resumes}">
        <tr>
            <td>${resume.id}</td>
            <td>${resume.applicant.fullName}</td>
            <td>${resume.position.name}</td>
            <td>${resume.minSalary}</td>
            <td><c:choose><c:when test="${resume.active}">актуально</c:when><c:otherwise>архив</c:otherwise></c:choose></td>
            <td><a href="<c:url value='/resumes/${resume.id}'/>">Открыть</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="_footer.jspf" %>
