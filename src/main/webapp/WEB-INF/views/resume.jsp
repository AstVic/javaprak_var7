<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Резюме"/>
<%@ include file="_header.jspf" %>

<h2>Резюме #${resume.id}</h2>
<p><strong>Соискатель:</strong> ${resume.applicant.fullName}</p>
<p><strong>Контакты:</strong> ${resume.applicant.contactInfo}</p>
<p><strong>Должность:</strong> ${resume.position.name}</p>
<p><strong>Желаемая зарплата:</strong> ${resume.minSalary}</p>

<p>
    <a class="button" href="<c:url value='/resumes/${resume.id}/matches'/>">Подобрать вакансии</a>
    <a class="button" href="<c:url value='/resumes/${resume.id}/edit'/>">Редактировать</a>
</p>
<form method="post" action="<c:url value='/resumes/${resume.id}/delete'/>">
    <button class="danger" type="submit">Удалить</button>
</form>

<section class="section">
    <h3>Образование</h3>
    <table>
        <tr>
            <th>Уровень</th>
            <th>Место</th>
            <th>Направление</th>
        </tr>
        <c:forEach var="education" items="${resume.educationRecords}">
            <tr>
                <td>${education.level.name}</td>
                <td>${education.institution}</td>
                <td>${education.major}</td>
            </tr>
        </c:forEach>
    </table>
</section>

<section class="section">
    <h3>История работы</h3>
    <table data-testid="work-history">
        <tr>
            <th>Компания</th>
            <th>Должность</th>
            <th>Начало</th>
            <th>Окончание</th>
            <th>Зарплата</th>
        </tr>
        <c:forEach var="work" items="${resume.workExperiences}">
            <tr>
                <td>${work.companyName}</td>
                <td>${work.position.name}</td>
                <td>${work.startDate}</td>
                <td><c:choose><c:when test="${empty work.endDate}">по настоящее время</c:when><c:otherwise>${work.endDate}</c:otherwise></c:choose></td>
                <td>${work.salary}</td>
            </tr>
        </c:forEach>
    </table>
</section>

<%@ include file="_footer.jspf" %>
