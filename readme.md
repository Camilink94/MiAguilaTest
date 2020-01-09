## Mi Aguila Test App

Para esta prueba se usó MVP como patrón arquitectónico.

Los paquetes se dividieron así:
- Dentro del paquete `view` se encuentra la actividad (que implementa la vista)
- Dentro del paquete `presenter` se encuentra el Presenter, 
    que además tiene el contrato (interfaz) de la vista.
- Dentro del paquete `location` se encuentra el Repo para la información de la ubicación, 
    así como el contrato del Repo para el presenter
- Dentro del paquete `injection` se encuentra el módulo de inyección de Koin

La actividad (vista) tiene un presenter inyectado, así como el presenter
tiene un repo inyectado. La DI se hace con Koin por facilidad de uso, 
por estar escrita en Kotlin, y estar lista para Android.

El repo de GIT se manejó con GitFlow, y siguiendo la guía de 
[Semantic Commit Messages](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)

#### Notas

En el proyecto no se usan los sensores para la actualización de la velocidad, 
pero se dejan los métodos listos para hacerlo.

Después de un tiempo de actualización, el mapa puede ponerse lento. Esto
se puede deber a la elección de `CircleOptions` para mostrar la ubicación.