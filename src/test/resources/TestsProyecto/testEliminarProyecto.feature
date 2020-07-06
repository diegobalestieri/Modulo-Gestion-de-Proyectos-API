Feature: Eliminación de un proyecto

  Scenario: Borro una x cantidad de proyectos
    Given existen 10 proyectos cargados en el sistema
    When elimino 5 proyectos
    Then quedan 5 elementos cargados en el sistema
