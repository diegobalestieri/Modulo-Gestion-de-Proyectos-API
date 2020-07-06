Feature: Atributos de un proyecto

  Scenario: Verifico el nombre de un proyecto
    Given un proyecto creado con cierto nombre "Nuevo Proyecto"
    When pregunto el nombre del proyecto
    Then se me devuelve el nombre correcto

  Scenario: Verifico los tipos de proyectos
    Given un listado de proyectos
    When creo proyectos de distinto tipo
    | nombre |      tipo      |
    | imp_1  | Implementación |
    | des_1  | Desarrollo     |
    | des_2  | Desarrollo     |
    | imp_2  | Implementación |

    Then se me devuelven los tipos correctos para cada proyecto
      | nombre |      tipo      |
      | imp_1  | Implementación |
      | des_1  | Desarrollo     |
      | des_2  | Desarrollo     |
      | imp_2  | Implementación |