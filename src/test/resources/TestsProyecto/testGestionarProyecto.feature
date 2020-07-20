Feature: Gestionar Proyecto

  Background:
    Given un listado con proyectos cargados
      |         nombre        |      tipo      |
      | Sistema ERP Mulesoft  | Implementación |
      |          ERP          | Desarrollo     |
      |          BI           | Desarrollo     |
      | Sistema CRM MicroTec  | Implementación |

  Scenario: Modifico el estado de un proyecto recién cargado
    Given selecciono el proyecto "ERP"
    When modifico su estado a "Finalizado"
    Then el estado del proyecto es el correcto

  Scenario: Intento cambiar el estado de un proyecto iniciado a No iniciado
    Given selecciono el proyecto "ERP" con estado "Activo"
    When modifico su estado a "No iniciado"
    Then se lanza un error indicando que el estado no se pudo cambiar
    And el estado del proyecto sigue siendo "Activo"

  Scenario: Asigno la fecha de inicio de un proyecto que no la tiene
    Given selecciono el proyecto "Sistema ERP Mulesoft"
    When asigno la fecha de inicio a "2020-07-10"
    Then la fecha de inicio del proyecto es "2020-07-10"

  Scenario: Reasignar fecha de inicio lanza error
    Given selecciono un proyecto y le asigno la fecha de inicio "2020-07-10"
    And cambio el estado de proyecto a activo
    When asigno la fecha de inicio a "2020-11-11"
    Then se lanza un error indicando que la fecha de inicio no se puede modificar
    And la fecha de inicio del proyecto es "2020-07-10"

  Scenario: Asignar una fecha de inicio con formato inválido lanza un error
    Given selecciono un proyecto
    When asigno la fecha de inicio a "2020-9a-99"
    Then se lanza un error indicando que la fecha de inicio ingresada no es válida

  Scenario:  Modificar el nombre y descripción de un proyecto los cambia correctamente
    Given selecciono el proyecto "ERP"
    When le cambio el nombre a "Posta" y descripcion "Esto no es una prueba"
    Then el nombre del proyecto es "Posta"
    And la descripción es "Esto no es una prueba"

  Scenario: Guardar un proyecto con una determinada fecha de inicio se guarda correctamente
    Given creo un proyecto con fecha de inicio "2020-07-10"
    When pregunto la fecha de inicio del proyecto
    Then la fecha "2020-07-10" se guardo correctamente

  Scenario: Asignar un producto a un proyecto de desarrollo
    Given selecciono un proyecto de Desarrollo
    When lo asocio al producto "ERP Cloud" y lo guardo
    Then el proyecto tiene el producto asociado "ERP Cloud"
  Scenario: Modificar el producto a un proyecto de desarrollo que ya tiene uno
    Given selecciono un proyecto de Desarrollo
    And lo asocio al producto "Psa BI" y lo guardo
    When lo asocio al producto "ERP Cloud" y lo guardo
    Then el proyecto tiene el producto asociado "ERP Cloud"

