<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Форма резюме"/>
<%@ include file="_header.jspf" %>

<h2><c:choose><c:when test="${mode == 'edit'}">Редактирование резюме</c:when><c:otherwise>Создание резюме</c:otherwise></c:choose></h2>

<c:choose>
    <c:when test="${mode == 'edit'}">
        <c:url var="actionUrl" value="/resumes/${resume.id}"/>
    </c:when>
    <c:otherwise>
        <c:url var="actionUrl" value="/resumes"/>
    </c:otherwise>
</c:choose>

<form method="post" action="${actionUrl}" data-testid="resume-form">
    <label for="applicantId">Соискатель</label>
    <select id="applicantId" name="applicantId" required>
        <c:forEach var="applicant" items="${applicants}">
            <option value="${applicant.id}" ${resume.applicant.id == applicant.id ? 'selected' : ''}>${applicant.fullName}</option>
        </c:forEach>
    </select>

    <label for="positionId">Должность</label>
    <select id="positionId" name="positionId" required>
        <c:forEach var="position" items="${positions}">
            <option value="${position.id}" ${resume.position.id == position.id ? 'selected' : ''}>${position.name}</option>
        </c:forEach>
    </select>

    <label for="minSalary">Желаемая зарплата</label>
    <input id="minSalary" name="minSalary" type="number" value="${empty resume ? 0 : resume.minSalary}" required>

    <label>
        <input name="active" type="checkbox" value="true" ${empty resume || resume.active ? 'checked' : ''}>
        актуально
    </label>

    <button type="submit">Сохранить</button>
</form>

<%@ include file="_footer.jspf" %>
