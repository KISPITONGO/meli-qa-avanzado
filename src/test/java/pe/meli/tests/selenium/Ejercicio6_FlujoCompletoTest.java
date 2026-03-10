package pe.meli.tests.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pe.meli.pages.SearchPage;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ejercicio 6 - Flujo completo encadenado en Selenium
 *
 * CONCEPTO CLAVE:
 *  Un solo test simula un flujo real de usuario:
 *  Buscar → Aplicar filtro → Verificar resultados filtrados → Abrir producto → Validar detalle
 *
 *  Usamos WebDriverWait + ExpectedConditions para esperas inteligentes
 *  (no Thread.sleep, que es frágil y lento).
 */
@DisplayName("Ejercicio 6 - Flujo completo encadenado")
class Ejercicio6_FlujoCompletoTest extends BaseTest {

    @Test
    @DisplayName("Flujo: buscar auriculares → filtrar por Nuevo → abrir primer producto → validar detalle")
    void testFlujoCompletoConFiltro() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        SearchPage searchPage = new SearchPage(driver);

        // ── PASO 1: Buscar producto ─────────────────────────────────────────
        searchPage.buscar("auriculares");
        System.out.println("✓ PASO 1: Búsqueda realizada");

        // ── PASO 2: Aplicar filtro "Nuevo" ──────────────────────────────────
        boolean filtroAplicado = searchPage.aplicarFiltro("Nuevo");
        if (filtroAplicado) {
            System.out.println("✓ PASO 2: Filtro 'Nuevo' aplicado");
        } else {
            System.out.println("⚠ PASO 2: Filtro no encontrado, continuando sin él");
        }

        // ── PASO 3: Verificar que sigue habiendo resultados ─────────────────
        int resultadosFiltrados = searchPage.contarResultadosVisibles();
        System.out.println("✓ PASO 3: Resultados tras filtro: " + resultadosFiltrados);
        assertFalse(resultadosFiltrados == 0,
                "No hay resultados después de aplicar el filtro 'Nuevo'");

        // ── PASO 4: Guardar el título del primer resultado y clickearlo ──────
        String tituloPrimerProducto = searchPage.clickearResultado(0);
        System.out.println("✓ PASO 4: Producto clickeado → " + tituloPrimerProducto);

        // ── PASO 5: Verificar que la página de detalle cargó ────────────────
        // Esperar a que el título del browser cambie (indica que la navegación ocurrió)
        wait.until(ExpectedConditions.not(
                ExpectedConditions.titleContains("auriculares | MercadoLibre")));

        String tituloPagina = driver.getTitle();
        System.out.println("✓ PASO 5: Página de detalle cargada → " + tituloPagina);

        // La URL debe haber cambiado a la página del producto
        String urlActual = driver.getCurrentUrl();
        assertTrue(
                urlActual.contains("mercadolibre") || urlActual.contains("mercadopago"),
                "La URL no corresponde a MercadoLibre: " + urlActual
        );
    }

    @Test
    @DisplayName("Flujo: comparar precios entre 2 búsquedas distintas")
    void testCompararPreciosEntreBusquedas() {
        SearchPage searchPage = new SearchPage(driver);

        // Buscar el primero y guardar precio
        searchPage.buscar("mouse gaming");
        int precioMouseGaming = searchPage.obtenerPrecioMasBarato();
        System.out.println(">>> Precio más bajo mouse gaming: S/ " + precioMouseGaming);

        // Volver al home y buscar otro producto
        driver.get(BASE_URL);
        searchPage.buscar("mouse básico");
        int precioMouseBasico = searchPage.obtenerPrecioMasBarato();
        System.out.println(">>> Precio más bajo mouse básico:  S/ " + precioMouseBasico);

        // Ambos deben tener precios válidos
        assertAll(
                () -> assertNotEquals(-1, precioMouseGaming, "No se encontró precio para mouse gaming"),
                () -> assertNotEquals(-1, precioMouseBasico, "No se encontró precio para mouse básico")
        );

        // El mouse gaming debería ser más caro (o igual) que el básico
        // No falla el test si no se cumple, solo lo reporta (puede haber ofertas)
        if (precioMouseGaming < precioMouseBasico) {
            System.out.println("ℹ️  El mouse gaming resultó más barato. Puede haber ofertas activas.");
        } else {
            System.out.println("✓  El mouse gaming cuesta igual o más que el básico (lógico).");
        }
    }

    @Test
    @DisplayName("Flujo: buscar → verificar que la URL contiene el término buscado")
    void testURLContieneTerminoBuscado() {
        WebDriverWait wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        SearchPage searchPage = new SearchPage(driver);

        String termino = "laptop lenovo";
        searchPage.buscar(termino);

        // Esperar a que la URL se actualice con el término de búsqueda
        wait.until(ExpectedConditions.urlContains("laptop"));

        String urlActual = driver.getCurrentUrl().toLowerCase();
        System.out.println(">>> URL después de búsqueda: " + urlActual);

        assertTrue(urlActual.contains("laptop"),
                "La URL no contiene el término buscado. URL: " + urlActual);
    }

    @Test
    @DisplayName("Flujo: buscar producto → abrir detalle → verificar que hay botón de comprar")
    void testDetalleProductoTieneBotonComprar() {
        WebDriverWait wait    = new WebDriverWait(driver, Duration.ofSeconds(12));
        SearchPage searchPage = new SearchPage(driver);

        searchPage.buscar("silla de escritorio");
        searchPage.clickearResultado(0);

        // En la página de detalle, buscar el botón de compra
        // MercadoLibre usa distintos textos según el tipo de producto
        List<WebElement> botonesCompra = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//*[contains(text(),'Comprar ahora') " +
                                "or contains(text(),'Agregar al carrito') " +
                                "or contains(text(),'Comprar')]")
                )
        );

        System.out.println(">>> Botones de compra encontrados: " + botonesCompra.size());
        assertFalse(botonesCompra.isEmpty(),
                "No se encontró ningún botón de compra en la página de detalle");
    }
}
