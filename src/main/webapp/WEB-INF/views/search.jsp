<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Подбор вакансий"/>
<%@ include file="_header.jspf" %>

<h2>Подбор вакансий и резюме</h2>

<form method="get" action="<c:url value='/search'/>" data-testid="search-form">
    <label for="mode">Режим</label>
    <select id="mode" name="mode">
        <option value="vacancies" ${mode != 'resumes' ? 'selected' : ''}>Вакансии</option>
        <option value="resumes" ${mode == 'resumes' ? 'selected' : ''}>Резюме</option>
    </select>

    <label for="companyId">Компания вакансии</label>
    <select id="companyId" name="companyId">
        <option value="">любая</option>
        <c:forEach var="company" items="${companies}">
            <option value="${company.id}">${company.name}</option>
        </c:forEach>
    </select>

    <label for="companyName">Компания из опыта работы</label>
    <input id="companyName" name="companyName" type="text">

    <label for="positionId">Должность</label>
    <select id="positionId" name="positionId">
        <option value="">любая</option>
        <c:forEach var="position" items="${positions}">
            <option value="${position.id}">${position.name}</option>
        </c:forEach>
    </select>

    <label for="educationLevelId">Уровень образования</label>
    <select id="educationLevelId" name="educationLevelId">
        <option value="">любой</option>
        <c:forEach var="level" items="${educationLevels}">
            <option value="${level.id}">${level.name}</option>
        </c:forEach>
    </select>

    <label for="minSalary">Зарплата от</label>
    <input id="minSalary" name="minSalary" type="number">

    <label for="maxSalary">Зарплата до</label>
    <input id="maxSalary" name="maxSalary" type="number">

    <label for="minExperienceMonths">Минимальный стаж, месяцев</label>
    <input id="minExperienceMonths" name="minExperienceMonths" type="number">

    <button type="submit">Найти</button>
</form>

<c:if test="${not empty vacancies}">
    <h3>Найденные вакансии</h3>
    <table data-testid="vacancies-results">
        <tr>
            <th>Компания</th>
            <th>Должность</th>
            <th>Зарплата</th>
            <th>Стаж</th>
            <th></th>
        </tr>
        <c:forEach var="vacancy" items="${vacancies}">
            <tr>
                <td>${vacancy.company.name}</td>
                <td>${vacancy.position.name}</td>
                <td>${vacancy.salary}</td>
                <td>${vacancy.minMonthsExperience}</td>
                <td><a href="<c:url value='/vacancies/${vacancy.id}'/>">Открыть</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${not empty resumes}">
    <h3>Найденные резюме</h3>
    <table data-testid="resumes-results">
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
</c:if>

<%@ include file="_footer.jspf" %>
