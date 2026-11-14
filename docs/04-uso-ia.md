Bitácora de Uso de Inteligencia Artificial (`docs/04-uso-ia.md`)
Este documento registra el uso estratégico, crítico y responsable de herramientas de Inteligencia Artificial (IA) durante el proceso de análisis, diseño, refactorización, pruebas y documentación de la modernización de Spring PetClinic, cumpliendo con los lineamientos exigidos en la prueba técnica.
1. Herramientas y Modelos Utilizados
   Herramienta Principal: Gemini (Google) / Asistente de IA conversacional.
   Entorno de Desarrollo: IDE (IntelliJ IDEA) con soporte de compilación en Java 21 y Spring Boot.
   Propósito: Apoyo en diseño de arquitectura hexagonal, definición de contratos RESTful, resolución de incidencias en tiempo de ejecución (JPA, manejo de excepciones, caché con Caffeine) y generación de pruebas unitarias.
2. Prompt Inicial de Contexto (System / Role Prompting)
   Para garantizar que las respuestas de la IA mantuvieran coherencia arquitectónica, evitaran soluciones genéricas o acopladas y respetaran los estándares del proyecto, se inició la sesión estableciendo un rol, alcance y stack tecnológico explícito:
> **Prompt Inicial:**
> *"Actúa como un Arquitecto de Software Senior especialista en Java 21 y el ecosistema Spring Boot. Estoy modernizando la aplicación legacy Spring PetClinic hacia una API RESTful utilizando un Monolito Modular basado en Arquitectura Hexagonal (Puertos y Adaptadores).*
>
> *Reglas estrictas de diseño:*
> *1. Separación total entre entidades de persistencia (`*JpaEntity`) y modelos de dominio puro (sin anotaciones de frameworks en el dominio).*
> *2. Mapeadores explícitos inyectados como beans de Spring (evitar métodos estáticos).*
> *3. Uso de DTOs de entrada/salida (`*Request`, `*Response`) encapsulados en un wrapper estándar (`ControllerResponse`).*
> *4. Manejo centralizado de errores mediante `@RestControllerAdvice` por módulo.*
> *5. Optimización de consultas con Spring Data JPA y caché en memoria con Caffeine.*
>
> *Ayúdame a diseñar, depurar y probar los módulos de Owners/Pets, Visits y Vets manteniendo estas restricciones."*
3. Interacciones Clave, Prompts y Validación Humana
   A continuación se documentan las consultas más representativas realizadas durante el desarrollo, las soluciones propuestas por la IA, y el criterio técnico aplicado para aceptarlas, corregirlas o rechazarlas.
   Caso 1: Eliminación de redundancia en contratos JSON y pureza del Dominio
   Objetivo: Evitar que el campo `ownerId` apareciera repetido dentro de la lista de mascotas (`pets`) cuando se consulta el detalle de un propietario (`OwnerResponse`), sin romper las validaciones internas de negocio.
   Prompt / Consulta:
> *"En el dominio `Pet`, la lógica de validación exige que `ownerId` no sea nulo. Intenté poner `@JsonIgnore` en el atributo de la clase `Pet`, pero al serializar la respuesta de `Owner`, el `ownerId` sigue apareciendo en el JSON. ¿Cómo puedo ocultarlo sin afectar la lógica?"*
Respuesta de la IA: Explicó que al usar `@Getter` de Lombok, Jackson lee el método `getOwnerId()` y no el atributo privado. Propuso dos alternativas: mover `@JsonIgnore` al getter (parche rápido) o crear un DTO de resumen (`PetSummaryResponse`) sin ese campo para mantener el dominio libre de anotaciones de infraestructura.
Decisión y Validación Humana: Se aceptó la segunda opción. Se descartó el uso de `@JsonIgnore` en el dominio para respetar la regla de Arquitectura Hexagonal (dominio agnóstico a librerías de serialización). Se implementó el mapeo hacia un DTO limpio y se verificó mediante peticiones HTTP `GET` que el JSON resultante no tuviera datos duplicados.
Caso 2: Resolución de propagación de errores HTTP 500 en lugar de 404
Objetivo: Lograr que al consultar las mascotas de un propietario inexistente (`OwnerNotFoundException`), la API respondiera con un código HTTP `404 Not Found` en lugar de un `500 Internal Server Error`.
Prompt / Consulta:
> *"Mi servicio `FindPetsByOwnerService` lanza correctamente `OwnerNotFoundException` (verificado en los logs), pero el cliente recibe un 500 genérico. Tengo un `OwnerControllerAdvice` con `@RestControllerAdvice(basePackages = \"...owners.web.handler\")`. ¿Por qué no está interceptando la excepción?"*
Respuesta de la IA: Identificó un error de alcance en la configuración del `basePackages`. El controlador estaba ubicado en `...owners.infrastructure.web.controller`, fuera del paquete configurado en el `Advice`, provocando que Spring ignorara el manejador y delegara la excepción al contenedor web (500).
Decisión y Validación Humana: Se aceptó y aplicó la corrección. Se ajustó el atributo `basePackages` de los `@RestControllerAdvice` de cada módulo (`owners`, `vets`, `visits`) para apuntar a sus respectivos paquetes base. Se validó ejecutando peticiones contra IDs inexistentes, confirmando el retorno del código `404` con la estructura `ErrorResponse`.
Caso 3: Configuración de Caché con Caffeine sobre JCache (JSR-107)
Objetivo: Incorporar límites de tamaño máximo y políticas de expiración en memoria para los datos maestros (`vets` y `petTypes`) reutilizando la clase base `CacheConfiguration` del proyecto original.
Prompt / Consulta:
> *"Quiero aprovechar la clase original `CacheConfiguration` que utiliza `JCacheManagerCustomizer` y `MutableConfiguration`, pero el estándar JCache no me permite definir `maximumSize` ni tiempos de expiración. ¿Cómo integro Caffeine manteniendo esta estructura?"*
Respuesta de la IA: Propuso reemplazar `MutableConfiguration` por `CaffeineConfiguration` (del paquete `com.github.ben-manes.caffeine.jcache.configuration`), lo que permite configurar `.setMaximumSize()` y `.expireAfterWrite()` manteniendo la integración nativa con `JCacheManagerCustomizer`. Además, sugirió crear un servicio de lectura dedicado (`FindPetTypesService`) para que el proxy de Spring pudiera interceptar la anotación `@Cacheable`.
Decisión y Validación Humana: Se aceptó con ajustes. Se integró `CaffeineConfiguration` para `"vets"` y `"petTypes"`, y se creó el endpoint `GET /api/v1/pets/types` respaldado por `FindPetTypesService`. Se verificó en los logs de consola que la primera petición ejecutaba la consulta SQL (`Cache miss`) y las subsecuentes respondían desde memoria sin tocar la base de datos.
Caso 4: Generación de Pruebas Unitarias aisladas por capa
Objetivo: Asegurar la cobertura de pruebas unitarias para el nuevo flujo de tipos de mascotas (`PetTypeController`, `FindPetTypesService`, `PetTypeRepositoryAdapter`).
Prompt / Consulta:
> *"Genera las pruebas unitarias con JUnit 5 y Mockito para el controlador, el servicio con `@Cacheable` y el adaptador de persistencia de `PetType`, respetando la inyección por constructor."*
Respuesta de la IA: Generó tres clases de prueba utilizando `@ExtendWith(MockitoExtension.class)` para el servicio y el adaptador, y `@WebMvcTest` con `MockMvc` para el controlador, validando tanto los mapeos como la estructura JSON de `ControllerResponse`.
Decisión y Validación Humana: Se aceptó y adaptó. Se revisaron las aserciones (`jsonPath`) para que coincidieran exactamente con los nombres de campos del DTO `ControllerResponse` del proyecto y se ejecutó la suite completa mediante el wrapper de Maven/Gradle para confirmar el éxito de todos los tests.
4. Limitaciones Detectadas y Criterio Crítico
   Durante el uso de la IA se identificaron y corrigieron las siguientes limitaciones:
   Manejo de nulos en mapeadores: En una iteración inicial para resolver un `NullPointerException` en `OwnerEntityMapper`, la IA sugirió envolver retornos en `Optional.ofNullable` de forma genérica. Esta sugerencia fue rechazada/corregida en favor de inicializar las colecciones vacías de forma segura (`List.of()`) y mantener las validaciones estrictas de invariantes dentro del constructor del dominio.
   Acoplamiento de frameworks: La IA tendía ocasionalmente a sugerir anotaciones de Jackson o Spring Data directamente en las clases de dominio para resolver problemas rápidos de serialización. Se aplicó criterio arquitectónico para mantener dichas preocupaciones exclusivamente en la capa de `infrastructure`.
5. Declaración de Responsabilidad y Verificación
   Todo el código, configuraciones y documentación asistidos por IA fueron auditados, refactorizados y probados manualmente por el candidato. Se verificó la compilación limpia en Java 21, la ejecución exitosa de las pruebas automatizadas y el cumplimiento funcional respecto a las reglas de negocio originales de Spring PetClinic.
