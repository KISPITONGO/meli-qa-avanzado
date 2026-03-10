@ignore
Feature: Utilidad reutilizable de busqueda (NO ejecutar directamente)
  # Este feature se llama con "call read(...)" desde otros features
  # Las variables productoQuery e itemLimit deben venir del caller
  # @ignore evita que Karate lo ejecute solo durante el test run

  Scenario: buscar producto
    Given url 'https://dummyjson.com'
    And path '/products/search'
    And param q     = productoQuery
    And param limit = itemLimit
    When method GET
    Then status 200
    # El response queda disponible en el contexto del feature que llamo
