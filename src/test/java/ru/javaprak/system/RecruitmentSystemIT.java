package ru.javaprak.system;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecruitmentSystemIT {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = System.getProperty("app.baseUrl");
        Assumptions.assumeTrue(baseUrl != null && !baseUrl.isBlank(),
                "Set -Dapp.baseUrl=http://localhost:8080/javaprak7 to run Selenium system tests");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void navigationPagesAreAvailable() {
        driver.get(baseUrl + "/");
        assertTrue(driver.getPageSource().contains("Кадровое агентство"));

        driver.findElement(By.linkText("Размещение резюме")).click();
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Размещение резюме"));

        driver.findElement(By.linkText("Подбор вакансий")).click();
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Подбор вакансий"));

        driver.findElement(By.linkText("Важная информация")).click();
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Повторный отклик"));
    }

    @Test
    void resumeSearchReturnsResults() {
        driver.get(baseUrl + "/search?mode=resumes&positionId=1&maxSalary=250000");
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-testid='resumes-results']")));
        assertTrue(table.getText().contains("Java-разработчик"));
    }

    @Test
    void vacancySearchReturnsResults() {
        driver.get(baseUrl + "/search?mode=vacancies&positionId=1&minSalary=200000");
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-testid='vacancies-results']")));
        assertTrue(table.getText().contains("Java-разработчик"));
    }

    @Test
    void invalidSalaryRangeShowsError() {
        driver.get(baseUrl + "/search?mode=vacancies&minSalary=300000&maxSalary=100000");
        assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='error']"))).getText()
                .contains("Минимальная зарплата не может превышать максимальную"));
    }

    @Test
    void resumeCardShowsWorkHistory() {
        driver.get(baseUrl + "/resumes/1");
        assertTrue(driver.findElement(By.cssSelector("[data-testid='work-history']")).getText()
                .contains("ООО"));
    }

    @Test
    void matchingVacanciesForResumeAreShown() {
        driver.get(baseUrl + "/resumes/1/matches");
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Подходящие вакансии"));
    }

    @Test
    void vacancyCardAllowsResponseAndRejectsDuplicate() {
        driver.get(baseUrl + "/vacancies/1");
        new Select(driver.findElement(By.id("resumeId"))).selectByValue("1");
        driver.findElement(By.cssSelector("[data-testid='response-form'] button")).click();
        assertTrue(driver.findElement(By.tagName("body")).getText()
                .contains("уже существует"));
    }

    @Test
    void invalidResumeCreationShowsError() {
        driver.get(baseUrl + "/resumes/new");
        driver.findElement(By.id("minSalary")).clear();
        driver.findElement(By.id("minSalary")).sendKeys("-10");
        driver.findElement(By.cssSelector("[data-testid='resume-form'] button")).click();
        assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='error']"))).getText()
                .contains("Желаемая зарплата не может быть отрицательной"));
    }
}
