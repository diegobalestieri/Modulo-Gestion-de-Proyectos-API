Feature: Creacion de Iteracion

  Background:
    Given cuento con un proyecto activo con una fase y tareas cargadas

  Scenario: Creo una iteracion
    When creo una iteracion con los siguientes datos
      |    nombre   | fecha de inicio | fecha de finalizacion |
      | Iteracion 1 |   2020-08-15    |     2020-09-15       |
    Then la iteracion cuenta con los datos correctos

  Scenario:Modifico los datos de una iteracion
    Given creo una iteracion con los siguientes datos
      |    nombre   | fecha de inicio | fecha de finalizacion |
      | Iteracion 1 |   2020-08-15    |     2020-09-15       |
    When modifico los datos de la iteracion
      |    nombre   | fecha de inicio | fecha de finalizacion |
      | Iteracion 1 |   2020-06-17    |     2020-08-19      |
    Then la iteracion cuenta con los datos correctos

  Scenario:Agrego las tareas del proyecto a una iteracion
    Given creo una iteracion y le agrego las tareas del proyecto
    When consulto las tareas de la iteracion
    Then se me devuelven las tareas correctas

  Scenario: Borro una iteracion
    Given creo una iteracion en una fase
    When borro la iteracion
    Then la fase ya no cuenta con la iteracion

 Scenario: Elimino tareas de una iteracion
    Given creo una iteracion y le agrego las tareas del proyecto
    When elimino las tareas de la iteracion
    Then la iteracion no cuenta con las tareas cargadas