<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Подходящие резюме"/>
<%@ include file="_header.jspf" %>

<h2>Подходящие резюме для вакансии #${vacancy.id}</h2>

<c:choose>
    <c:when test="${empty resumes}">
        <p data-testid="empty-result">Подходящие резюме не найдены.</p>
    </c:when>
    <c:otherwise>
        <table data-testid="matching-resumes">
            <tr>
                <th>Соискатель</th>
                <th>Должность</th>
                <th>Желаемая зарплата</th>
                <th></th>
            </tr>
            <c:forEach var="resume" items="${resumes}">
                <tr>
                    <td>${resume.applicant.fullName}</td>
                    <td>${resume.position.name}</td>
                    <td>${resume.minSalary}</td>
                    <td><a href="<c:url value='/resumes/${resume.id}'/>">Открыть</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="_footer.jspf" %>
