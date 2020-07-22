Feature: Gestion de tareas

	Scenario: Crear una tarea en el proyect Backlog
		Given tengo un proyecto creado
		When creo una tarea con fecha de finalización, nombre, descripción y prioridad
			| fecha de fin | nombre | descripcion | prioridad |
			| 2020-10-20 | Implementar fases | Agregar la funcionalidad de crear fases | Muy alta |
		Then la tarea se agrega al proyect backlog
		And contiene los datos correspondientes
		And su estado es “No iniciado”
		And su fecha de creación es la del día de la fecha en que fue creada