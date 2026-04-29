<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Вакансия"/>
<%@ include file="_header.jspf" %>

<h2>Вакансия #${vacancy.id}</h2>
<p><strong>Компания:</strong> ${vacancy.company.name}</p>
<p><strong>Контакты:</strong> ${vacancy.company.contactInfo}</p>
<p><strong>Должность:</strong> ${vacancy.position.name}</p>
<p><strong>Зарплата:</strong> ${vacancy.salary}</p>
<p><strong>Минимальный стаж:</strong> ${vacancy.minMonthsExperience} месяцев</p>
<p><strong>Минимальное образование:</strong> ${vacancy.minEducationLevel.name}</p>

<p><a class="button" href="<c:url value='/vacancies/${vacancy.id}/matches'/>">Подобрать резюме</a></p>

<section class="section">
    <h3>Откликнуться</h3>
    <form method="post" action="<c:url value='/responses'/>" data-testid="response-form">
        <input type="hidden" name="vacancyId" value="${vacancy.id}">
        <label for="resumeId">Резюме</label>
        <select id="resumeId" name="resumeId" required>
            <c:forEach var="resume" items="${resumes}">
                <option value="${resume.id}">${resume.id}: ${resume.applicant.fullName}, ${resume.position.name}</option>
            </c:forEach>
        </select>
        <button type="submit">Откликнуться</button>
    </form>
</section>

<%@ include file="_footer.jspf" %>
