<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Подходящие вакансии"/>
<%@ include file="_header.jspf" %>

<h2>Подходящие вакансии для резюме #${resume.id}</h2>

<c:choose>
    <c:when test="${empty vacancies}">
        <p data-testid="empty-result">Подходящие вакансии не найдены.</p>
    </c:when>
    <c:otherwise>
        <table data-testid="matching-vacancies">
            <tr>
                <th>Компания</th>
                <th>Должность</th>
                <th>Зарплата</th>
                <th></th>
            </tr>
            <c:forEach var="vacancy" items="${vacancies}">
                <tr>
                    <td>${vacancy.company.name}</td>
                    <td>${vacancy.position.name}</td>
                    <td>${vacancy.salary}</td>
                    <td><a href="<c:url value='/vacancies/${vacancy.id}'/>">Открыть</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="_footer.jspf" %>
