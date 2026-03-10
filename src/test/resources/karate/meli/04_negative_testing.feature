Feature: Ejercicio 4 - Pruebas Negativas (Negative Testing)
  # Nivel avanzado: no solo probamos el happy path, tambien validamos
  # que la API se comporta correctamente ante entradas invalidas o edge cases

  Background:
    * url 'https://dummyjson.com'

  Scenario: Buscar con termino inexistente debe retornar 0 resultados
    Given path '/products/search'
    And param q = 'xyzproductoinexistente99999'
    When method GET
    Then status 200
    And match response.total == 0
    And match response.products == '#[0]'

  Scenario: Obtener producto con ID invalido debe retornar 404
    Given path '/products/99999'
    When method GET
    Then status 404
    And match response.message == '#string'
    * print '>>> Mensaje de error: ' + response.message

  Scenario: Acceder a una categoria que no existe debe manejarse correctamente
    Given path '/products/category/categoria-inexistente-xyz'
    When method GET
    Then status 200
    # Retorna un array vacio de productos
    And match response.products == '#[0]'
    And match response.total == 0

  Scenario: El endpoint de categorias debe retornar una lista no vacia
    Given path '/products/categories'
    When method GET
    Then status 200
    # Es un array de objetos, cada uno con slug y name
    And match response == '#[] #object'
    And match response[0] contains { slug: '#string', name: '#string' }
    * def totalCategorias = response.length
    * assert totalCategorias > 5
    * print '>>> Total de categorias: ' + totalCategorias

  Scenario: Buscar con limit=0 - la API usa un valor por defecto
    Given path '/products/search'
    And param q     = 'laptop'
    And param limit = 0
    When method GET
    Then status 200
    # DummyJSON ignora limit=0 y retorna con un limite por defecto
    * def cantidadProductos = response.products.length
    * assert cantidadProductos > 0
    * print '>>> Productos con limit=0 (API usa default): ' + cantidadProductos

  Scenario: Validar headers de response (Content-Type debe ser JSON)
    Given path '/products/search'
    And param q = 'chair'
    When method GET
    Then status 200
    And match responseHeaders['Content-Type'][0] contains 'application/json'
