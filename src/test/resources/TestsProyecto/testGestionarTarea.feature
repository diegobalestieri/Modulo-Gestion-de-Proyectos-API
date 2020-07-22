Feature: Creacion de Tarea

  Background:
    Given tengo una tarea creada con nombre "Validar requisitos"

  Scenario: Verifico los datos de la tarea
    When pregunto el nombre y el estado de la tarea
    Then el nombre de la tarea es "Validar requisitos"
    And el estado de la tarea es "No iniciada"


  Scenario: Modifico el estado de la tarea
    When modifico el estado de la tarea a "En curso"
    Then el estado de la tarea es "En curso"

  Scenario: Modifico los datos de la tarea
    When modifico el nombre de la tarea a "Iniciar fase de Desarrollo"
    And modifico la descripcion de la tarea a "Empieza el desarrollo de codigo"
    Then el nombre de la tarea es "Iniciar fase de Desarrollo"
    And la descripcion de la tarea es "Empieza el desarrollo de codigo"

  Scenario: Asigno una fecha de inicio valida a una tarea
    When asigno la fecha de inicio de la tarea a "2020-07-15"
    Then la tarea tiene la fecha de inicio correcta

  Scenario: Asigno una fecha de inicio invalida a una tarea
    When asigno la fecha de inicio de la tarea a "202x-07-15"
    Then se lanza un error indicando que la fecha ingresada no es valida
    And la fecha de inicio de la tarea no se modificó

  Scenario: Asigno una fecha de finalizacion valida a una tarea
    When asigno la fecha de finalizacion de la tarea a "2020-07-19"
    Then la tarea tiene la fecha de finalizacion correcta

  Scenario: Asigno una fecha de finalizacion erronea a una tarea
    When asigno la fecha de finalizacion de la tarea a "202x-07-19"
    Then se lanza un error indicando que la fecha ingresada no es valida
    And la fecha de finalizacion de la tarea no se modificó

  Scenario: Asigno una fecha de finalizacion a una tarea anterior a la de su inicio
    When asigno la fecha de inicio de la tarea a "2020-07-15"
    And asigno la fecha de finalizacion de la tarea a "2020-07-13"
    Then se lanza un error indicando que la fecha de finalizacion no puede ser anterior a la de inicio
    And la fecha de finalizacion de la tarea no se modificó