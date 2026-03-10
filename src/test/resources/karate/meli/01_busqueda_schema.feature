Feature: Ejercicio 1 - Buscar productos con validacion de esquema (Schema Validation)
  # Nivel avanzado: validamos la ESTRUCTURA completa del response, no solo un campo
  # Usamos #string, #number, #array, #regex para tipar cada campo
  # API: DummyJSON (publica, sin autenticacion)

  Background:
    * url 'https://dummyjson.com'

  Scenario: Buscar "laptop" y validar que cada resultado tiene la estructura correcta
    Given path '/products/search'
    And param q = 'laptop'
    And param limit = 5
    When method GET
    Then status 200

    # Validaciones del nivel raiz del response
    * assert response.total > 0
    And match response.limit == 5

    # Validar que hay exactamente 5 resultados en el array
    And match response.products == '#[5]'

    # match each: aplica la validacion a CADA elemento del array
    And match each response.products contains
      """
      {
        id:                  '#number',
        title:               '#string',
        price:               '#number',
        category:            '#string',
        thumbnail:           '#string',
        rating:              '#number',
        stock:               '#number',
        brand:               '#string',
        availabilityStatus:  '#string'
      }
      """

    # Imprimir el titulo del primer resultado para verificar manualmente
    * print 'Primer resultado: ' + response.products[0].title
    * print 'Precio: $' + response.products[0].price

  Scenario: Buscar con limit=1 y validar campo de reviews y dimensiones
    Given path '/products/search'
    And param q = 'phone'
    And param limit = 1
    When method GET
    Then status 200

    # Validar que reviews es un array de objetos con la estructura esperada
    And match response.products[0].reviews == '#[] #object'
    And match each response.products[0].reviews contains
      """
      {
        rating:       '#number',
        comment:      '#string',
        reviewerName: '#string'
      }
      """

    # Validar que dimensions tiene la estructura correcta
    And match response.products[0].dimensions contains
      """
      {
        width:  '#number',
        height: '#number',
        depth:  '#number'
      }
      """
