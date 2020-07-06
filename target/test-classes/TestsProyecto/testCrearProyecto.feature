Feature: Creacion de un proyecto

  Scenario: Creo proyectos en el sistema
    Given un listado de proyectos vacio
    When creo algunos proyectos con nombre e id
      | 1 | prueba1 |
      | 2 | adada   |
      | 4 | adsjkla |
    Then el listado de proyectos pasa a tener 3 elementos.
