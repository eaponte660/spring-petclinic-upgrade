# Plan de Migración e Incrementalidad - spring-petclinic Modular

El objetivo de este documento es definir la estrategia de refactorización para transformar el monolito legacy acoplado de spring-petclinic en un **Monolito Modular con Arquitectura Hexagonal**, asegurando cero tiempo de inactividad funcional, alta testabilidad y un riesgo controlado durante el proceso.

---

## 1. Estrategia de Migración Incremental (Fases por Bounded Context)

Para evitar un enfoque de "reescritura total" (*Big Bang*), la migración se ejecutará de forma incremental aislando un dominio a la vez:

* **Fase 1: Preparación y Núcleo Común (`owners`)**
  * *Acción:* Extraer el dominio, puertos y adaptadores del módulo de propietarios y mascotas (`owners`).
  * *Validación:* Pruebas unitarias de dominio puras con JUnit 5 y pruebas de integración del repositorio JPA mediante slices de Spring (`@DataJpaTest`).
* **Fase 2: Extracción del Dominio Veterinario (`vets`)**
  * *Acción:* Migrar la gestión del personal médico y especialidades al nuevo paquete modular aplicando contratos independientes.
  * *Validación:* Verificación de que no existan dependencias circulares directas con el módulo `owners`.
* **Fase 3: Extracción del Módulo de Visitas Clínicas (`visits`)**
  * *Acción:* Modularizar el historial médico y las visitas de las mascotas, conectándolas mediante referencias por ID o eventos de aplicación internos si es estrictamente necesario.
  * *Validación:* Pruebas de integración de flujo completo (Crear dueño -> Crear mascota -> Registrar visita).
* **Fase 4: Limpieza Final y Retiro del Legacy**
  * *Acción:* Eliminar controladores de Spring MVC antiguos, vistas de Thymeleaf obsoletas y clases del monolito acoplado. Consolidar la API REST pura.

---

## 2. Mapeo de Casos de Uso Originales vs. Nuevos Endpoints REST

La siguiente tabla detalla la correspondencia entre los controladores legacy basados en Spring MVC/Thymeleaf y los nuevos adaptadores REST desacoplados:

| Dominio | Caso de Uso Original (Legacy Spring MVC) | Nuevo Endpoint REST Target | Puerto de Entrada (Use Case / Command) |
| :--- | :--- | :--- | :--- |
| **Owners** | `OwnerController.initCreationForm` / `processCreationForm` | `POST /api/v1/owners` | `CreateOwnerUseCase` / `CreateOwnerCommand` |
| **Owners** | `OwnerController.initUpdateOwnerForm` / `processUpdateOwnerForm` | `PUT /api/v1/owners/{id}` | `UpdateOwnerUseCase` / `UpdateOwnerCommand` |
| **Owners** | `OwnerController.showOwner` (Vista HTML) | `GET /api/v1/owners/{id}` | `FindOwnerUseCase` / `GetOwnerQuery` |
| **Owners** | `OwnerController.initFindForm` / `processFindForm` | `GET /api/v1/owners?lastName={lastName}` | `SearchOwnersUseCase` / `SearchOwnersQuery` |
| **Pets** | `PetController.processCreationForm` | `POST /api/v1/owners/{ownerId}/pets` | `CreatePetUseCase` / `CreatePetCommand` |
| **Pets** | `PetController.initUpdateForm` / `processUpdateForm` | `PUT /api/v1/owners/{ownerId}/pets/{petId}` | `UpdatePetUseCase` / `UpdatePetCommand` |
| **Visits** | `VisitController.initNewVisitForm` / `processNewVisitForm` | `POST /api/v1/owners/{ownerId}/pets/{petId}/visits` | `CreateVisitUseCase` / `CreateVisitCommand` |
| **Vets** | `VetController.showVetList` (HTML Tabla) | `GET /api/v1/vets` | `FindAllVetsUseCase` / `GetVetsQuery` |

### Criterio Técnico: Uso de `PUT` vs. `PATCH` para Operaciones de Actualización

Para las operaciones de actualización (`UpdateOwner` y `UpdatePet`), se ha seleccionado el verbo HTTP **`PUT`** en lugar de `PATCH` debido a los siguientes motivos:

1. **Naturaleza del Dominio (Entidades Ligeras):** Las entidades del dominio PetClinic (`Owner`, `Pet`) poseen un conjunto reducido de atributos (nombre, apellido, dirección, teléfono, etc.). Enviar la representación completa de la entidad no genera sobrecarga significativa en la red.
2. **Coherencia con el Flujo de Formularios:** El sistema proviene de una arquitectura basada en formularios web tradicionales donde el usuario edita el perfil completo y envía la estructura de datos consolidada. El contrato REST con `PUT` mapea de forma natural este comportamiento del cliente.
3. **Idempotencia:** `PUT` garantiza estrictamente la idempotencia (ejecutar la misma petición varias veces produce el mismo resultado en el servidor), lo que simplifica la gestión de reintentos y la predictibilidad de la API.
4. **Simplicidad Arquitectónica:** Evita la complejidad de implementar un motor de actualizaciones parciales (como JSON Patch RFC 6902 o la gestión de nulos condicionales para diferenciar entre campos omitidos y valores limpiados a propósito), optimizando los tiempos de entrega sin sacrificar calidad en una prueba de este alcance.

---

## 3. Estrategia de Acceso a Datos y Gestión de Transacciones

* **Aislamiento por Módulo:** Cada módulo (`owners`, `visits`, `vets`) administrará sus propias entidades de persistencia y repositorios de Spring Data. Aunque compartan la misma base de datos física durante la fase monolítica, **está estrictamente prohibido que un repositorio o entidad de un módulo sea accedido directamente por otro módulo**.
* **Transaccionalidad (`@Transactional`):** Las transacciones de base de datos se orquestarán exclusivamente en la capa de aplicación (en las implementaciones de los Casos de Uso / Servicios), garantizando que las reglas de consistencia de negocio se ejecuten dentro de una única frontera transaccional.

---

## 4. Plan de Validación y Testing

Para garantizar una calidad técnica impecable, cada incremento se validará bajo tres niveles de prueba:
1. **Pruebas Unitarias de Dominio (Fast Unit Tests):** Validación de la lógica de negocio pura sin levantar el contexto de Spring Boot.
2. **Pruebas de Adaptadores de Persistencia:** Uso de H2 en memoria y `@DataJpaTest` para verificar el correcto funcionamiento de los adaptadores JPA y mappers.
3. **Pruebas de API REST (Integration Tests):** Pruebas de integración HTTP utilizando `MockMvc` o `TestRestTemplate` para validar los contratos JSON, códigos de estado HTTP y el manejo global de errores.
