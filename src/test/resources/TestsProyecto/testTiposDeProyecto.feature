Feature: Tipos de proyecto

  Background:

  Given un listado con proyectos cargados de distinto tipo
  | nombre |      tipo      | cliente/producto |
  | imp_1  | Implementación | Mulesoft |
  | des_1  | Desarrollo     | ERP |
  | des_2  | Desarrollo     | BI |
  | imp_2  | Implementación | Microsoft |

  Scenario: Verifico los tipos de proyectos
    When pregunto el tipo de cada proyecto
    Then se me devuelven los tipos correctos

  Scenario: Agrego un cliente a un proyecto de Implementacion
    When agrego al cliente "Mulesoft" a un proyecto de Implementacion
    Then el cliente "Mulesoft" se agrego al proyecto correctamente

  Scenario: Agrego un producto a un proyecto de Desarrollo
    When agrego al producto "ERP" a un proyecto de Desarrollo
    Then el producto "ERP" se agrego al proyecto correctamente