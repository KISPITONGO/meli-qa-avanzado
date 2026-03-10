package pe.meli.tests.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Clase base para todos los tests de Selenium.
 *
 * Centraliza la configuración del driver para que los tests no repitan código.
 * Cada test hereda de esta clase y tiene su propio driver (paralelo-safe).
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://www.mercadolibre.com.pe";

    /**
     * Se ejecuta UNA sola vez antes de todos los tests de la clase.
     * WebDriverManager descarga automáticamente el ChromeDriver correcto.
     */
    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    /**
     * Se ejecuta ANTES de cada test individual.
     * Crea un browser limpio sin historial ni cookies.
     */
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Descomenta la siguiente línea para ejecutar en modo headless (sin ventana):
        // options.addArguments("--headless=new");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        driver.get(BASE_URL);
    }

    /**
     * Se ejecuta DESPUÉS de cada test.
     * Cierra el browser aunque el test haya fallado.
     */
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
