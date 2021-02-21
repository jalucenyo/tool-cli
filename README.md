# Tool-Cli

Este proyecto es una colección de utilidades que nos brinda una "caja de herramientas" que automatiza tareas cotidianas al desarrollar un proyecto.

Tool-cli se ejecuta desde un terminal con diferentes comandos y un fichero de configuración en formato json que parametriza el comportamiento de cada uno de los comandos.

## Requisitos

Para poder ejecutar Tool-Cli, necesitas tener instalado lo siguiete:

- Java 11
- Docker

## Inicio rápido

Para comenzar un nuevo proyecto lo ejecutaremos indicando el parametro "init" y el nombre del proyecto. Tool-Cli creara una carpeta con el nombre y la estructura de carpetas, asi como un fichero de configuración básico.

```sh
tootl-cli init my_proyect_env
cd my_proyect_env
```
  
## Archivo de configuración

### Repositorios de los proyectos "clone".

Podemos gestionar varios proyectos, que dependen entre ellos para la aplicación que estamos desarrollando, el comando "clone" nos permite clonar una lista de repositorios con un unico comando, en el carchivo de configuración indicaremos la lista de repositorios que queremos clonar y la carpeta de destino, todos son clonados bajo la carpeta "services".

Ejemplo de la sección del fichero de configuración.

```json
"clone": {
  "repositories" : [
    { "url": "https://repository.git", "folder" : "project1" },
    { "url": "https://repository.git", "folder" : "project2" }
  ]
}
```
Para ejecutarlos nos situamos en la carpeta del proyecto y ejecutamos:

```sh
cd my_proyect_env
tootl-cli clone
```


  
