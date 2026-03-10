package pe.meli.tests.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.meli.pages.SearchPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ejercicio 5 - Selenium con Page Object Model (POM)
 *
 * CONCEPTO CLAVE:
 *  Los tests NO contienen localizadores CSS/XPath.
 *  Todo eso está en SearchPage. Los tests solo hablan en términos de negocio:
 *  "buscar()", "contarResultadosVisibles()", "obtenerPrecioMasBarato()"
 */
@DisplayName("Ejercicio 5 - Page Object Model (POM)")
class Ejercicio5_POMTest extends BaseTest {

    @Test
    @DisplayName("Buscar 'laptop' y verificar que hay al menos 5 resultados visibles")
    void testBusquedaConResultados() {
        SearchPage searchPage = new SearchPage(driver);

        searchPage.buscar("laptop");

        int totalResultados = searchPage.contarResultadosVisibles();

        System.out.println(">>> Resultados encontrados: " + totalResultados);
        assertTrue(totalResultados >= 5,
                "Se esperaban al menos 5 resultados, pero se encontraron: " + totalResultados);
    }

    @Test
    @DisplayName("Buscar 'teclado' y verificar que el precio más barato es un número válido")
    void testPrecioMasBaratoEsValido() {
        SearchPage searchPage = new SearchPage(driver);

        searchPage.buscar("teclado");

        int precioMasBarato = searchPage.obtenerPrecioMasBarato();

        System.out.println(">>> Precio más barato encontrado: S/ " + precioMasBarato);
        assertNotEquals(-1, precioMasBarato,
                "No se encontró ningún precio en la página");
        assertTrue(precioMasBarato > 0,
                "El precio debe ser mayor que 0, se obtuvo: " + precioMasBarato);
    }

    @Test
    @DisplayName("Buscar 'auriculares' y verificar que los títulos contienen la palabra buscada")
    void testTitulosRelacionadosConBusqueda() {
        SearchPage searchPage = new SearchPage(driver);

        searchPage.buscar("auriculares");

        List<String> titulos = searchPage.obtenerTitulos();

        System.out.println(">>> Títulos obtenidos: " + titulos.size());
        assertFalse(titulos.isEmpty(), "La lista de títulos no debería estar vacía");

        // Al menos la mitad de los resultados debe mencionar "auricular" en el título
        long relacionados = titulos.stream()
                .filter(t -> t.toLowerCase().contains("auricular")
                          || t.toLowerCase().contains("audifono")
                          || t.toLowerCase().contains("audífono")
                          || t.toLowerCase().contains("headset")
                          || t.toLowerCase().contains("headphone")
                          || t.toLowerCase().contains("earbuds")
                          || t.toLowerCase().contains("earphone")
                          || t.toLowerCase().contains("bluetooth"))
                .count();

        System.out.println(">>> Títulos relacionados: " + relacionados + " de " + titulos.size());
        assertTrue(relacionados >= titulos.size() / 2,
                "Menos de la mitad de los resultados están relacionados con 'auriculares'");
    }

    @Test
    @DisplayName("Buscar un término muy específico y verificar que hay al menos 1 resultado")
    void testBusquedaEspecificaTieneResultados() {
        SearchPage searchPage = new SearchPage(driver);

        searchPage.buscar("mouse logitech inalambrico");

        int total = searchPage.contarResultadosVisibles();

        System.out.println(">>> Resultados para búsqueda específica: " + total);
        assertTrue(total >= 1,
                "Se esperaba al menos 1 resultado para 'mouse logitech inalambrico'");
    }
}
