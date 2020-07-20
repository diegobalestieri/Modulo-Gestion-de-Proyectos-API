Feature: Creacion de Fase

  Background:
    Given cuento con un proyecto activo

  Scenario:Creo una fase para el proyecto
    When creo una fase para el proyecto con los siguientes datos
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 1 | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
    Then la fase se agrega al proyecto con los datos correspondientes.
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 1 | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |

  Scenario: Creo una fase en un proyecto que se persiste
    When creo una fase para el proyecto con los siguientes datos y lo guardo
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Descubrimiento | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
      | Disenio | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
      | Desarrollo | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
    Then la fase guardada se agrega al proyecto con los datos correspondientes.
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Descubrimiento | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
      | Disenio | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |
      | Desarrollo | Esta es la primera fase del proyecto | 09-10-2020 | 09-15-2020 |

  Scenario: Creo una fase en un proyecto a travez de la API
    Given tengo el siguiente proyecto creado
      | Nombre | Descripcion | tipo |
      | Proyecto de Desarrollo | Este es un proyecto de desarrollo | Desarrollo |
    When creo una fase para el proyecto desde la API con los siguientes datos
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 1 | Esta es la primera fase del proyecto | 2020-04-16 | 2020-05-24 |
    Then cuando obtengo la fase a travez de la api tiene los datos correctos
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 1 | Esta es la primera fase del proyecto | 2020-04-16 | 2020-05-24 |
    And cuando la elimino deja de existir en el proyecto
      | nombre  | descripcion | fecha de inicio | fecha de finalizacion |
      | Fase 1 | Esta es la primera fase del proyecto | 2020-04-16 | 2020-05-24 |