# Módulo Gestion de Proyectos API
Este repositorio corresponde a la API del modulo de gestion de proyectos que sera deployado en Heroku
Para acceder a la documentación de la API ir a [http://localhost:8080//swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Como correr Spring  
Opción A:
> ./mvnw spring-boot:run  

Opción B:
> ./mvnw clean package  (esto genera un archivo JAR ejecutable)
> java -jar target/gs-rest-service-0.1.0.jar  

Luego acceder a [http://localhost:8080/](http://localhost:8080/) con el endpoint correspondiente.

## Otros detalles  
Los controladores se marcan con la anotación `@RestController`.
Para vincular un determinado endpoint (url) se usan anotacion como `@GetMapping` para GET y `@PostMapping` para POST. Por ejemplo:
> http://localhost:8080/proyecto --> crea un proyecto nuevo con el id siguiente.  

Para recibir parámetros desde la url se usa `@RequestParam`
> http://localhost:8080/proyecto?nombre=Sistema de gestión --> crea un proyecto con el nombre sistema de gestión  

Para visualizar los proyectos escribir esto en una terminal, no en la que se corre la API:
> curl -v GET localhost:8080/proyectos  

Para crear un nuevo proyecto escribir esto en una terminal, no en la que se corre la API:
> curl -v POST localhost:8080/proyectos -H 'Content-type:application/json' -d '{"id":..., "nombre":"...", "tipoDeProyecto": "Desarrollo" o "Implementación"}'  

## Definiciones en la Aplicación de Spring
Los **componentes** que esten en otra carpeta (por ejemplo controladores) deben agregarse con la anotación `@ComponentScan(basePackageClasses = <clase>)` en la Aplicación. Donde <clase> es una clase incluida en el paquete donde se encuentra el componente que se quiere agregar.  
Las **entidades** que esten en otra carpeta deben agregarse con `@EntityScan(basePackageClasses = <clase>)`. Donde <clase> es una clase incluida en el paquete donde se encuentra el componente que se quiere agregar.  
Los **repositorios** que esten en otra carpeta deben agregarse con `@EnableJpaRepositories(basePackageClasses = <clase>)`. Donde <clase> es una clase incluida en el paquete donde se encuentra el componente que se quiere agregar.  

## Crear base de datos de postgreSQL y usuario para conectarse
>$ sudo -u postgres createuser datosaurio  
$ sudo -u postgres createdb prueba  
$ sudo -u postgres psql  
psql=# alter user datosaurio with encrypted password 'corona';  
psql=# grant all privileges on database prueba to datosaurio ;    
  
### Entidades  
- A las entidades hay que agregarles un constructor sin .  
- Las entidades siempre tienen ID > 0.  
  
### Servicios  
- Los métodos de servicios que utilizan más de una vez al repositorio de datos deben ser @Transactional.  