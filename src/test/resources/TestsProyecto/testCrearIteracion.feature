Feature: Creacion de Iteracion

  Background:
    Given cuento con un proyecto activo con una fase
    And tengo una lista de iteraciones con los siguientes datos
      |    nombre   | fecha de inicio | fecha de finalizacion |
      | Iteracion 1 |   09-10-2020    |     09-15-2020        |
      | Iteracion 2 |   09-10-2020    |     09-15-2020        |
      | Iteracion 3 |   09-10-2020    |     09-15-2020        |

  Scenario:Agrego iteraciones a la fase
    When agrego las iteraciones a la fase
    Then las iteraciones se agregaron correctamente
