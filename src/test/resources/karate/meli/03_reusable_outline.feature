Feature: Ejercicio 3 - Features reutilizables + Scenario Outline (Data-Driven)
  # Tecnica avanzada: reutilizar logica con "call read" y probar multiples
  # casos de una sola vez usando Scenario Outline + Examples table

  Background:
    * url 'https://dummyjson.com'

  #---------------------------------------------------------------------
  # PARTE A: Usar el feature reutilizable con distintos productos
  #---------------------------------------------------------------------
  Scenario Outline: Buscar "<producto>" y validar que hay resultados
    * def productoQuery = '<producto>'
    * def itemLimit     = 3

    # Llamamos al feature reutilizable — recibimos su response en nuestro contexto
    * call read('util_busqueda.feature')

    # Ahora usamos el response como si lo hubieramos hecho aqui mismo
    And match response.products == '#[3]'

    * def totalResultados = response.total
    * assert totalResultados > 0
    * print '>>> "' + productoQuery + '" -> ' + totalResultados + ' resultados en total'

    Examples:
      | producto   |
      | phone      |
      | laptop     |
      | fragrance  |
      | shirt      |
      | oil        |

  #---------------------------------------------------------------------
  # PARTE B: Scenario Outline validando distintos limites de paginacion
  #---------------------------------------------------------------------
  Scenario Outline: Buscar con limit=<limite> y verificar que response respeta el limite
    Given path '/products/search'
    And param q     = 'laptop'
    And param limit = <limite>
    When method GET
    Then status 200
    And match response.limit    == <limite>
    And match response.products == '#[<limite>]'

    Examples:
      | limite |
      | 1      |
      | 3      |
      | 5      |
