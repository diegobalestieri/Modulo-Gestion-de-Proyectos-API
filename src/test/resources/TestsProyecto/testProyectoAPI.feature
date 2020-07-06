Feature: Proyecto API

  Scenario: Crear un proyecto con todos sus datos lo crea correctamente.
    Given tengo el listado de proyectos
    #noinspection NonAsciiCharacters
    When Creo un proyecto con nombre, descripcion y tipo
      | Nombre | Descripcion | tipo |
      | Proyecto de Desarrollo | Este es un proyecto de desarrollo | Desarrollo |
      #| Proyecto de Implementacion | Este es un proyecto de implementacion | Implementación |
    Then el proyecto se crea con los datos correctos
      | Nombre | Descripcion | tipo |
      | Proyecto de Desarrollo | Este es un proyecto de desarrollo | Desarrollo |
      #| Proyecto de Implementacion | Este es un proyecto de implementacion | Implementación |