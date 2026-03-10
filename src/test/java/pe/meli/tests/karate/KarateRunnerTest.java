package pe.meli.tests.karate;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.DisplayName;

/**
 * Runner principal de Karate para IntelliJ IDEA.
 *
 * CÓMO EJECUTAR:
 *   - Click derecho sobre la clase → "Run 'KarateRunnerTest'"
 *   - O desde la terminal: mvn test -Dtest=KarateRunnerTest
 *
 * Para ejecutar UN solo feature:
 *   - Click derecho sobre el método correspondiente (ej: ejercicio1)
 */
public class KarateRunnerTest {

    private static final String FEATURES_PATH = "classpath:karate/meli";

    @Karate.Test
    @DisplayName("Ejercicio 1 - Schema Validation")
    Karate ejercicio1_schemaValidation() {
        return Karate.run(FEATURES_PATH + "/01_busqueda_schema.feature");
    }

    @Karate.Test
    @DisplayName("Ejercicio 2 - API Chaining")
    Karate ejercicio2_chaining() {
        return Karate.run(FEATURES_PATH + "/02_chaining.feature");
    }

    @Karate.Test
    @DisplayName("Ejercicio 3 - Reusable Feature + Scenario Outline")
    Karate ejercicio3_reusableOutline() {
        return Karate.run(FEATURES_PATH + "/03_reusable_outline.feature");
    }

    @Karate.Test
    @DisplayName("Ejercicio 4 - Negative Testing")
    Karate ejercicio4_negativeTesting() {
        return Karate.run(FEATURES_PATH + "/04_negative_testing.feature");
    }

    /**
     * Ejecuta TODOS los features de karate/meli de una sola vez.
     * Los reportes HTML se generan en target/karate-reports/
     */
    @Karate.Test
    @DisplayName("Todos los ejercicios Karate")
    Karate todosLosEjercicios() {
        return Karate.run(FEATURES_PATH);
    }
}
