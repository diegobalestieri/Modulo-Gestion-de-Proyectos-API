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

	Scenario: Creo una fase en un proyecto a traves de la API
		Given tengo el siguiente proyecto creado
			| nombre | descripcion | tipo |
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

    Scenario: Asignar fecha de inicio previa a la fecha de inicio del proyecto
	    Given cuento con un proyecto cargado con fecha de inicio "20/05/2020"
	    When creo una fase en el proyecto con una fecha de inicio "2020-01-01"
	    Then se lanza un error indicando que la fecha de inicio de la fase no puede ser anterior a la del proyecto que la contiene
	    And  la fase no se crea

