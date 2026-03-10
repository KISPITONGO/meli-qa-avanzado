Feature: Ejercicio 2 - Encadenamiento de llamadas API (API Chaining)
  # Tecnica avanzada: el resultado de una llamada alimenta a la siguiente
  # Paso 1: buscar un producto -> obtener su ID
  # Paso 2: usar ese ID para consultar el detalle completo

  Background:
    * url 'https://dummyjson.com'

  Scenario: Buscar "laptop" y obtener el detalle del primer resultado
    #------------------------------------------------------------------
    # LLAMADA 1: Busqueda general para obtener el ID del primer item
    #------------------------------------------------------------------
    Given path '/products/search'
    And param q = 'laptop'
    And param limit = 1
    When method GET
    Then status 200

    # Guardamos el ID y titulo para usarlos en la siguiente llamada
    * def firstItemId    = response.products[0].id
    * def firstItemTitle = response.products[0].title
    * def firstItemPrice = response.products[0].price
    * print '>>> Item encontrado: ' + firstItemId + ' | ' + firstItemTitle

    #------------------------------------------------------------------
    # LLAMADA 2: Detalle completo del item usando el ID obtenido
    #------------------------------------------------------------------
    Given path '/products/' + firstItemId
    When method GET
    Then status 200

    # Validar que el detalle corresponde al item buscado
    And match response.id    == firstItemId
    And match response.title == firstItemTitle

    # Validar estructura adicional solo disponible en el detalle completo
    And match response.images     == '#[] #string'
    And match response.reviews    == '#[] #object'
    And match response.dimensions contains { width: '#number', height: '#number', depth: '#number' }
    And match response.meta       contains { barcode: '#string' }

    * print '>>> Precio en busqueda: $' + firstItemPrice
    * print '>>> Precio en detalle:  $' + response.price

  Scenario: Encadenar busqueda -> categoria -> productos de esa categoria
    Given path '/products/search'
    And param q = 'fragrance'
    And param limit = 1
    When method GET
    Then status 200
    * def categoria = response.products[0].category

    # Llamar al endpoint de productos por categoria usando la categoria obtenida
    Given path '/products/category/' + categoria
    And param limit = 3
    When method GET
    Then status 200
    And match response.products == '#[3]'
    # Todos los productos deben ser de la misma categoria
    And match each response.products contains { category: '#(categoria)' }
    * print '>>> Categoria encontrada: ' + categoria + ' con ' + response.total + ' productos'
