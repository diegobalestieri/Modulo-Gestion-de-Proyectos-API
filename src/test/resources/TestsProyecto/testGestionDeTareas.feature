Feature: Gestion de tareas

	Background:
		Given tengo un proyecto creado

	Scenario: Crear una tarea en el proyect Backlog
		When creo una tarea con fecha de finalización, nombre, descripción y prioridad
			| fecha de fin | nombre | descripcion | prioridad |
			| 2020-10-20 | Implementar fases | Agregar la funcionalidad de crear fases | Muy alta |
		Then la tarea se agrega al proyect backlog
		And la tarea contiene los datos correspondientes
		And su estado es “No iniciado”
		And su fecha de creación es la del día de la fecha en que fue creada

	Scenario: Modificar datos de una tarea
		Given cuento con una tarea cargada en el proyecto
		When modifico los siguientes datos de la tarea
			| fecha de fin | nombre | descripcion | prioridad |
			| 2020-09-22 | Definir alcance | Definir el alcance del sprint | Alta |
		Then la tarea contiene los datos correspondientes

	Scenario: Eliminar una tarea
		Given cuento con una tarea cargada en el proyecto
		When elimino la tarea
		Then la tarea ya no se encuentra en el proyecto
