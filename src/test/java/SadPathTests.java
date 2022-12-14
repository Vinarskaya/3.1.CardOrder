import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SadPathTests {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    // проверка негативного сценария из-за неправильно введенного имени
    public void shouldShowErrorMessageIfName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Винарская Т.");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    // проверка негативного сценария c незаполненным полем ввода имени
    public void shouldShowErrorMessageIfNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    // проверка негативного сценария из-за неправильно введенного номера телефона
    public void shouldShowErrorMessageIfPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Винарская Татьяна");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+799");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    // проверка негативного сценария c незаполненным полем ввода номера телефона
    public void shouldShowErrorMessageIfPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Винарская Татьяна");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    // проверка негативного сценария из-за невыставленной галочки в чекбоксе
    public void shouldShowErrorMessageIfCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Винарская Татьяна");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector(".button__text")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim();

        assertEquals(expected, actual);
    }
}
