# Diagnóstico del Código Legacy - spring-petclinic

## 1. Tecnologías y Versiones Encontradas (Legacy)
* **Lenguaje:** Java 17 (especificado explícitamente en los archivos `build.gradle` y `pom.xml` mediante toolchain y propiedades).
* **Framework Principal:** Spring Boot (versión base del proyecto original con Spring Boot parent `4.1.0`).
* **Gestor de Dependencias / Compilación:** Soporte dual configurado con:
  * **Maven:** Mediante Maven Wrapper (wrapper version `3.3.4` y distribución `3.9.16`).
  * **Gradle:** Mediante Gradle Wrapper (distribución `9.5.1`).
* **Capa Web y Presentación:** Spring MVC con plantillas **Thymeleaf** (`spring.thymeleaf.mode=HTML`), WebJars (Bootstrap 5 y Font Awesome).
* **Persistencia y ORM:** 
  * **Motores Soportados:** Configuración multiperfil para **H2** (por defecto en memoria o archivos), **MySQL** y **PostgreSQL**.
  * **ORM:** Spring Data JPA / Hibernate, con estrategias de nombres físicas en snake_case y `spring.jpa.open-in-view=false` para optimización de sesiones.
* **Monitoreo y Calidad:** Spring Boot Actuator con exposición completa de endpoints web de gestión (`management.endpoints.web.exposure.include=*`), además de reglas de validación estática con Checkstyle y Spring Java Format.
* **Validación:** Bean Validation (`jakarta.validation`).

## 2. Infraestructura y Despliegue (Legacy)
* **Contenedorización Local (Docker Compose):** 
  * Definido en el archivo `docker-compose.yml`.
  * Incluye servicios preconfigurados para **MySQL (versión `9.7`)** y **PostgreSQL (versión `18.4`)**, exponiendo los puertos estándar (`3306` y `5432`).
* **Orquestación en Kubernetes (`k8s/`):**
  * **Manifiesto de Aplicación (`petclinic.yml`):** Configurado como un despliegue (`Deployment`) con un contenedor que apunta a la imagen `dsyer/petclinic`, usando perfiles activos de PostgreSQL, sondeos de salud (`livenessProbe`/`readinessProbe` en `/livez` y `/readyz`) y soporte para enlaces de servicios (*Service Bindings*).
  * **

## 3. Funcionalidades y Dominios de Negocio (Legacy)
El sistema monolítico actual gestiona un dominio veterinario acotado compuesto por cuatro entidades principales:
* **Propietarios (`Owner`):** Gestión de datos de contacto y demográficos de los dueños de mascotas.
* **Mascotas (`Pet`):** Relación de 1 a N con los propietarios, tipificadas por un tipo de mascota (`PetType`) y contenedor de un historial clínico de visitas.
* **Visitas (`Visit`):** Registros cronológicos de atención clínica asociados a una mascota específica.
* **Veterinarios (`Vet`):** Personal médico de la clínica asociado a especialidades profesionales (`Specialty`).

## 4. Mapeo de Endpoints y Controladores Legacy (Spring MVC / Thymeleaf)
El monolito expone su lógica mediante controladores basados en Spring MVC (`@Controller`), utilizando renderizado del lado del servidor (SSR) con Thymeleaf y redirecciones HTTP basadas en modelos de vista:

### Gestión de Propietarios (`OwnerController`)
* `GET /owners/find` — Vista de formulario para la búsqueda de propietarios por apellido.
* `GET /owners` — Vista de tabla con los resultados de la búsqueda.
* `GET /owners/{ownerId}` — Vista de detalle de un propietario específico y sus mascotas asociadas.
* `GET /owners/new` / `POST /owners/new` — Formulario y procesamiento para el registro de nuevos propietarios.
* `GET /owners/{ownerId}/edit` / `POST /owners/{ownerId}/edit` — Formulario y procesamiento para la actualización de datos de un propietario.

### Gestión de Mascotas (`PetController`)
* `GET /owners/{ownerId}/pets/new` / `POST /owners/{ownerId}/pets/new` — Formulario y procesamiento para agregar una nueva mascota a un propietario.
* `GET /owners/{ownerId}/pets/{petId}/edit` / `POST /owners/{ownerId}/pets/{petId}/edit` — Formulario y procesamiento para modificar la información de una mascota existente.

### Gestión de Visitas (`VisitController`)
* `GET /owners/{ownerId}/pets/{petId}/visits/new` / `POST /owners/{ownerId}/pets/{petId}/visits/new` — Formulario y procesamiento para registrar una nueva visita clínica a una mascota, validando restricciones de fecha mediante `BindingResult`.

### Consulta de Veterinarios (`VetController`)
* `GET /vets` — Vista de listado general del cuerpo médico y sus especialidades.

## 5. Diagnóstico de Acoplamiento y Modelo de Interacción
* **Acoplamiento Fuerte:** Los controladores están fuertemente acoplados a la infraestructura web de Spring MVC mediante anotaciones como `@ModelAttribute`, manejo de sesiones implícitas y validaciones atadas a formularios HTML (`BindingResult`).

## 6. Reglas de Negocio y Validaciones Identificadas
* **Propietarios:** Los campos de nombre, apellido, dirección, ciudad y teléfono son obligatorios; el teléfono debe cumplir con formatos numéricos válidos de largo 10.
* **Mascotas:** Cada mascota pertenece obligatoriamente a un propietario y posee un nombre único por contexto o validaciones de duplicidad.
* **Visitas:** Las fechas de las visitas no pueden ser retroactivas en ciertos registros o deben validar la existencia previa de la mascota.
* **Arquitectura acoplada:** Los controladores HTTP devuelven vistas HTML de Thymeleaf directamente, mezclando la capa de presentación con la lógica de rutas y acoplando los modelos de persistencia JPA a las pantallas.

## 7. Riesgos de Migración y Decisiones a Tomar
* **Desacoplamiento de la Capa Web (Eliminación de SSR/Thymeleaf):** La transición hacia un backend API REST/JSON exigirá reemplazar todos los controladores MVC (`@Controller`) por `@RestController` que consuman y expongan DTOs. Esto implica eliminar por completo Thymeleaf, los formularios atados a `BindingResult` y componentes web como `PetTypeFormatter`, trasladando el manejo de errores hacia un manejador global (`@RestControllerAdvice`) con respuestas JSON estandarizadas y códigos de estado HTTP adecuados.
* **Desacoplamiento del Modelo de Dominio:** Será necesario decidir si las entidades puras del dominio (en la arquitectura hexagonal) mantendrán una jerarquía limpia sin anotaciones de persistencia, separándolas de las entidades JPA de infraestructura (`@MappedSuperclass`) para evitar fugas de abstracción.
* **Migración de Validaciones Programáticas:** Reglas personalizadas como las contenidas en `PetValidator` o las validaciones de fecha en `VisitController` deberán trasladarse de la capa web hacia los casos de uso del dominio o DTOs de entrada mediante Bean Validation (`jakarta.validation`) y excepciones de negocio.
