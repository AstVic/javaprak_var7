# Отчёт по этапу 2
## «Кадровое агентство: вакансии и резюме» (вариант 7)

## Что реализовано

1. **Классы хранимых объектов (Hibernate Entity) для всех таблиц предметной области:**
    - `Applicant` (`Соискатель`)
    - `Resume` (`Резюме`)
    - `Education` (`Образование`)
    - `WorkExperience` (`Опыт_работы`)
    - `Company` (`Компания`)
    - `Vacancy` (`Вакансия`)
    - `Response` (`Отклик`)
    - `EducationLevel` (`Уровень_образования`)
    - `Position` (`Должность`)
    - связи многие-ко-многим `Resume <-> Education` и `Resume <-> WorkExperience` через таблицы `Резюме_Образование` и `Резюме_Опыт`.

2. **Отображение БД на объекты:**
    - аннотациями JPA в entity-классах;
    - конфигурацией Hibernate в `src/main/resources/hibernate.cfg.xml`.

3. **DAO-классы с типовыми запросами:**
    - `ApplicantDao`
    - `ResumeDao`
    - `VacancyDao`
    - `EducationDao`
    - `WorkExperienceDao`
    - `ResponseDao`

4. **Модульные тесты на JUnit 5:**
    - отдельные тест-классы на каждый DAO;
    - проверки сделаны через `assert`.

5. **Сборка Maven:**
    - зависимости Hibernate, PostgreSQL JDBC, JUnit 5;
    - `maven-surefire-plugin` для запуска тестов.

## Примечания по требованиям
- Для unit-тестов используется in-memory БД **H2 в режиме PostgreSQL**.

## Команды

```bash
mvn test
mvn -Pdb-create-schema sql:execute
```
