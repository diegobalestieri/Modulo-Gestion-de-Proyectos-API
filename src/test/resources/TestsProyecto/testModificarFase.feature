Feature: Creacion de Fase

  Background:
    Given cuento con un proyecto activo

  Scenario: Modificar los datos de una fase
    Given tengo el siguiente proyecto creado
      | Nombre | Descripcion | tipo |
      | Proyecto de Desarrollo | Este es un proyecto de desarrollo | Desarrollo |
    When creo una fase para el proyecto desde la API con los siguientes datos
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 33 | Esta es una fase medio rara | 2020-10-16 | 2020-11-29 |
    And modifico el nombre, descripción, fecha de inicio o finalización de una fase ya creada
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 4 | Esta es cuarta fase | 2020-11-11 | 2020-12-22 |
    Then cuando obtengo la fase a travez de la api tiene los datos correctos
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 4 | Esta es cuarta fase | 2020-11-11 | 2020-12-22 |