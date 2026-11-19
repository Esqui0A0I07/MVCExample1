# MVCExample1 - Persistencia con MapDB, ObjectBox y Patrón Builder

## Autores
- Isaac Esquivel Alfaro
- Johel Fernandez Madrigal

## Descripción general
Este proyecto adapta el ejercicio original de arquitectura **MVC** con Maven (que originalmente se conectaba a PostgreSQL mediante JDBC) para trabajar con bases de datos embebidas (**MapDB 3.1.0** y **ObjectBox 5.4.2**), implementando el patrón de diseño **Builder** para alternar de forma sencilla entre ambos motores sin modificar la lógica de negocio, el controlador ni las vistas.

---

## 1. Ubicación exacta del patrón Builder en el código

El patrón **Builder** y los componentes desacoplados que lo respaldan se encuentran en las siguientes rutas del proyecto:

- **Builder (Constructor fluido):** `src/main/java/modelo/builder/PersonaDaoBuilder.java`
- **Enumeración de motores soportados:** `src/main/java/modelo/builder/TipoBaseDatos.java` (`MAPDB`, `OBJECTBOX`)
- **Interfaz DAO (Producto del Builder):** `src/main/java/modelo/dao/IPersonaDao.java`
- **Implementaciones concretas del DAO:**
  - **MapDB:** `src/main/java/modelo/dao/PersonaDaoMapDB.java` (almacena `PersonaVo` serializable en un `HTreeMap` local)
  - **ObjectBox:** `src/main/java/modelo/dao/PersonaDaoObjectBox.java` (opera sobre `BoxStore` y la entidad `src/main/java/modelo/entity/PersonaEntity.java`)
- **Capa de lógica desacoplada:** `src/main/java/modelo/Logica.java` (recibe el DAO mediante `setPersonaDao(IPersonaDao personaDao)`)
- **Punto de configuración e instanciación:** `src/main/java/Principal.java`, dentro del método `iniciar()`

---

## 2. Cómo funciona el cambio de base de datos

1. **Desacoplamiento por contrato:** `modelo.Logica` ya no instancia directamente un DAO específico de PostgreSQL; en su lugar, todas sus operaciones CRUD (`validarRegistro`, `validarConsulta`, `validarModificacion` y `validarEliminacion`) se comunican únicamente con la interfaz `IPersonaDao`.
2. **Aislamiento de capas:** Las vistas (`vista/VentanaPrincipal`, `vista/VentanaRegistro`, `vista/VentanaBuscar`) y el controlador (`controlador/Coordinador`) no requieren cambios al alternar de motor, porque desconocen qué base de datos está activa.
3. **Construcción centralizada con Builder:** `PersonaDaoBuilder` encapsula los parámetros propios de cada motor:
   - Para **MapDB**: ruta del archivo `.db`, nombre del mapa y activación de transacciones.
   - Para **ObjectBox**: ruta del directorio donde se crea el `BoxStore`.
4. **Inyección y cierre seguro:** Al invocar `.build()` en `Principal.java`, el Builder devuelve la instancia concreta de `IPersonaDao` correspondiente, se inyecta en `Logica` y se registra un *shutdown hook* (`personaDao::cerrar`) para cerrar la base de datos correctamente al finalizar la aplicación.

> **Nota:** MapDB (`datos/personas_mapdb.db`) y ObjectBox (`datos/objectbox-personas`) mantienen archivos de almacenamiento independientes en disco, por lo que los registros guardados en un motor no aparecen al cambiar al otro.

---

## 3. Ejemplo para cambiar entre MapDB y ObjectBox

Para cambiar de base de datos, abra `src/main/java/Principal.java`, ubique el método `iniciar()` y modifique únicamente la construcción de `PersonaDaoBuilder`:

### Usar MapDB
```java
IPersonaDao personaDao = new PersonaDaoBuilder()
        .usarMapDB()
        .conRutaArchivoMapDB("datos/personas_mapdb.db")
        .conNombreMapaMapDB("personas")
        .conTransaccionesMapDB(true)
        .build();
```

### Cambiar a ObjectBox
```java
IPersonaDao personaDao = new PersonaDaoBuilder()
        .usarObjectBox()
        .conRutaDirectorioObjectBox("datos/objectbox-personas")
        .build();
```

### Alternativa usando la enumeración `TipoBaseDatos`
```java
IPersonaDao personaDao = new PersonaDaoBuilder()
        .conTipoBaseDatos(TipoBaseDatos.OBJECTBOX) // Cambiar a TipoBaseDatos.MAPDB para usar MapDB
        .conRutaDirectorioObjectBox("datos/objectbox-personas")
        .build();
```

---

## 4. Compilación y ejecución con Maven

1. Asegúrese de que exista la carpeta `objectbox-models/` en la raíz del proyecto (ObjectBox guarda ahí el esquema `default.json`).
2. Compile el proyecto con Maven para que el procesador de anotaciones genere `modelo.entity.MyObjectBox` y `objectbox-maven-plugin` transforme el bytecode de la entidad:

```bash
mvn clean compile
```

*(En IntelliJ IDEA puede ejecutar `clean` y `compile` desde el panel **Maven > MVCExample1 > Lifecycle**).*

3. Ejecute la clase `Principal`. Si utiliza JDK 24 o superior y desea omitir la advertencia de carga de librerías nativas, agregue la siguiente opción de VM en la configuración de ejecución:

```text
--enable-native-access=ALL-UNNAMED
```
