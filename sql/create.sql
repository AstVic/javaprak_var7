START TRANSACTION;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `Отклик`;
DROP TABLE IF EXISTS `Резюме_Опыт`;
DROP TABLE IF EXISTS `Резюме_Образование`;
DROP TABLE IF EXISTS `Вакансия`;
DROP TABLE IF EXISTS `Опыт_работы`;
DROP TABLE IF EXISTS `Образование`;
DROP TABLE IF EXISTS `Резюме`;
DROP TABLE IF EXISTS `Компания`;
DROP TABLE IF EXISTS `Соискатель`;
DROP TABLE IF EXISTS `Должность`;
DROP TABLE IF EXISTS `Уровень_образования`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `Уровень_образования` (
    `ID_уровня` BIGINT NOT NULL AUTO_INCREMENT,
    `Название` TEXT NOT NULL,
    PRIMARY KEY (`ID_уровня`),
    UNIQUE KEY `uk_уровень_образования_название` (`Название`(255))
) ENGINE=InnoDB;

CREATE TABLE `Должность` (
    `ID_должности` BIGINT NOT NULL AUTO_INCREMENT,
    `Название` TEXT NOT NULL,
    PRIMARY KEY (`ID_должности`),
    UNIQUE KEY `uk_должность_название` (`Название`(255))
) ENGINE=InnoDB;

CREATE TABLE `Соискатель` (
    `ID_соискателя` BIGINT NOT NULL AUTO_INCREMENT,
    `ФИО` TEXT NOT NULL,
    `Контактная_информация` TEXT NOT NULL,
    `Дата_рождения` DATE NOT NULL,
    `Домашний_адрес` TEXT NOT NULL,
    `Статус` BOOLEAN NOT NULL,
    PRIMARY KEY (`ID_соискателя`)
) ENGINE=InnoDB;

CREATE TABLE `Компания` (
    `ID_компании` BIGINT NOT NULL AUTO_INCREMENT,
    `Название` TEXT NOT NULL,
    `Контактная_информация` TEXT NOT NULL,
    PRIMARY KEY (`ID_компании`),
    UNIQUE KEY `uk_компания_название` (`Название`(255))
) ENGINE=InnoDB;

CREATE TABLE `Резюме` (
    `ID_резюме` BIGINT NOT NULL AUTO_INCREMENT,
    `Соискатель` BIGINT NOT NULL,
    `ID_должности` BIGINT NOT NULL,
    `Желаемая_зарплата_мин` BIGINT NOT NULL,
    `Актуально` BOOLEAN NOT NULL,
    PRIMARY KEY (`ID_резюме`),
    CONSTRAINT `fk_резюме_соискатель`
        FOREIGN KEY (`Соискатель`) REFERENCES `Соискатель`(`ID_соискателя`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_резюме_должность`
        FOREIGN KEY (`ID_должности`) REFERENCES `Должность`(`ID_должности`),
    CONSTRAINT `chk_резюме_желаемая_зарплата_мин`
        CHECK (`Желаемая_зарплата_мин` >= 0)
) ENGINE=InnoDB;

CREATE TABLE `Образование` (
    `ID_образования` BIGINT NOT NULL AUTO_INCREMENT,
    `Уровень` BIGINT NOT NULL,
    `Место_учебы` TEXT NOT NULL,
    `Направление` TEXT NOT NULL,
    `Дата_начала` DATE NOT NULL,
    `Дата_окончания` DATE,
    PRIMARY KEY (`ID_образования`),
    CONSTRAINT `fk_образование_уровень`
        FOREIGN KEY (`Уровень`) REFERENCES `Уровень_образования`(`ID_уровня`),
    CONSTRAINT `chk_образование_даты`
        CHECK (`Дата_окончания` IS NULL OR `Дата_окончания` >= `Дата_начала`)
) ENGINE=InnoDB;

CREATE TABLE `Опыт_работы` (
    `ID_опыта` BIGINT NOT NULL AUTO_INCREMENT,
    `Компания` TEXT NOT NULL,
    `ID_должности` BIGINT NOT NULL,
    `Дата_начала` DATE NOT NULL,
    `Дата_окончания` DATE,
    `Зарплата` BIGINT NOT NULL,
    PRIMARY KEY (`ID_опыта`),
    CONSTRAINT `fk_опыт_должность`
        FOREIGN KEY (`ID_должности`) REFERENCES `Должность`(`ID_должности`),
    CONSTRAINT `chk_опыт_зарплата`
        CHECK (`Зарплата` >= 0),
    CONSTRAINT `chk_опыт_даты`
        CHECK (`Дата_окончания` IS NULL OR `Дата_окончания` >= `Дата_начала`)
) ENGINE=InnoDB;

CREATE TABLE `Вакансия` (
    `ID_вакансии` BIGINT NOT NULL AUTO_INCREMENT,
    `Компания` BIGINT NOT NULL,
    `ID_должности` BIGINT NOT NULL,
    `Зарплата` BIGINT NOT NULL,
    `Мин_стаж_месяцев` BIGINT NOT NULL,
    `Мин_уровень_образования` BIGINT NOT NULL,
    PRIMARY KEY (`ID_вакансии`),
    CONSTRAINT `fk_вакансия_компания`
        FOREIGN KEY (`Компания`) REFERENCES `Компания`(`ID_компании`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_вакансия_должность`
        FOREIGN KEY (`ID_должности`) REFERENCES `Должность`(`ID_должности`),
    CONSTRAINT `fk_вакансия_уровень`
        FOREIGN KEY (`Мин_уровень_образования`) REFERENCES `Уровень_образования`(`ID_уровня`),
    CONSTRAINT `chk_вакансия_зарплата`
        CHECK (`Зарплата` >= 0),
    CONSTRAINT `chk_вакансия_мин_стаж`
        CHECK (`Мин_стаж_месяцев` >= 0)
) ENGINE=InnoDB;

CREATE TABLE `Резюме_Образование` (
    `ID_резюме` BIGINT NOT NULL,
    `ID_образования` BIGINT NOT NULL,
    PRIMARY KEY (`ID_резюме`, `ID_образования`),
    CONSTRAINT `fk_резюме_образование_резюме`
        FOREIGN KEY (`ID_резюме`) REFERENCES `Резюме`(`ID_резюме`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_резюме_образование_образование`
        FOREIGN KEY (`ID_образования`) REFERENCES `Образование`(`ID_образования`)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Резюме_Опыт` (
    `ID_резюме` BIGINT NOT NULL,
    `ID_опыта` BIGINT NOT NULL,
    PRIMARY KEY (`ID_резюме`, `ID_опыта`),
    CONSTRAINT `fk_резюме_опыт_резюме`
        FOREIGN KEY (`ID_резюме`) REFERENCES `Резюме`(`ID_резюме`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_резюме_опыт_опыт`
        FOREIGN KEY (`ID_опыта`) REFERENCES `Опыт_работы`(`ID_опыта`)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Отклик` (
    `ID_отклика` BIGINT NOT NULL AUTO_INCREMENT,
    `ID_резюме` BIGINT NOT NULL,
    `ID_вакансии` BIGINT NOT NULL,
    `Статус` VARCHAR(32) NOT NULL,
    `Дата` DATE NOT NULL,
    PRIMARY KEY (`ID_отклика`),
    CONSTRAINT `fk_отклик_резюме`
        FOREIGN KEY (`ID_резюме`) REFERENCES `Резюме`(`ID_резюме`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_отклик_вакансия`
        FOREIGN KEY (`ID_вакансии`) REFERENCES `Вакансия`(`ID_вакансии`)
        ON DELETE CASCADE,
    CONSTRAINT `chk_отклик_статус`
        CHECK (`Статус` IN ('recommended','sent','rejected','accepted'))
) ENGINE=InnoDB;

COMMIT;