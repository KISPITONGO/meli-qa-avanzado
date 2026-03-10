package pe.meli.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Page Object Model para la página de búsqueda de MercadoLibre Perú.
 *
 * PATRÓN POM:
 *  - Los localizadores (By / @FindBy) están AQUÍ, no en los tests.
 *  - Los tests solo llaman métodos con nombres de negocio (buscar, obtenerPrecios…)
 *  - Si ML cambia el HTML, solo actualizamos esta clase, no los tests.
 */
public class SearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─── Localizadores (@FindBy = PageFactory style) ────────────────────────
    @FindBy(name = "as_word")
    private WebElement searchInput;

    @FindBy(css = "button[type='submit'], .nav-search-btn")
    private WebElement searchButton;

    @FindBy(css = ".ui-search-results .ui-search-layout__item")
    private List<WebElement> searchResults;

    @FindBy(css = ".andes-money-amount__fraction")
    private List<WebElement> priceFractions;

    // ────────────────────────────────────────────────────────────────────────

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(12));
        PageFactory.initElements(driver, this);
    }

    /**
     * Escribe el término en el buscador y presiona Enter.
     */
    public void buscar(String termino) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(termino);
        searchInput.sendKeys(Keys.ENTER);
    }

    /**
     * Espera a que aparezcan resultados y devuelve cuántos hay visibles en la página.
     */
    public int contarResultadosVisibles() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        return searchResults.size();
    }

    /**
     * Devuelve la lista de títulos de los productos visibles en la página.
     */
    public List<String> obtenerTitulos() {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        return searchResults.stream()
                .map(r -> {
                    try {
                        return r.findElement(By.cssSelector(
                                ".ui-search-item__title, .poly-component__title")).getText();
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(t -> !t.isEmpty())
                .toList();
    }

    /**
     * Devuelve el precio más bajo encontrado en la página (como double, incluyendo decimales).
     * Si no hay precios visibles devuelve -1.0.
     */
    public double obtenerPrecioMasBarato() {
        wait.until(ExpectedConditions.visibilityOfAllElements(priceFractions));
        return priceFractions.stream()
                .map(WebElement::getText)
                .map(t -> t.replaceAll("[^0-9,.]", "").replace(",", "."))
                .filter(t -> !t.isEmpty())
                .mapToDouble(Double::parseDouble)
                .min()
                .orElse(-1.0);
    }

    /**
     * Hace clic en el filtro que contiene el texto dado (ej: "Nuevo", "Usado").
     * Retorna true si encontró y clickeó el filtro.
     */
    public boolean aplicarFiltro(String textoFiltro) {
        try {
            WebElement filtro = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[normalize-space()='" + textoFiltro + "']")));
            filtro.click();
            return true;
        } catch (Exception e) {
            System.out.println("⚠ Filtro no encontrado: " + textoFiltro);
            return false;
        }
    }

    /**
     * Hace clic en el n-ésimo resultado (index 0 = primero).
     * Devuelve el título del producto clickeado.
     */
    public String clickearResultado(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        WebElement item  = searchResults.get(index);
        WebElement titulo = item.findElement(By.cssSelector(
                ".ui-search-item__title, .poly-component__title"));
        String textoTitulo = titulo.getText();
        titulo.click();
        return textoTitulo;
    }
}
