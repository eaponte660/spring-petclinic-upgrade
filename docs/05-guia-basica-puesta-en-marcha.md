# 🚀 Guía de Puesta en Marcha y Ejecución

Esta guía detalla los prerrequisitos, configuración del entorno, comandos de compilación/ejecución y pruebas funcionales para levantar el backend modernizado de Spring PetClinic.

---

## 1. Prerrequisitos
Asegúrate de tener instalado en tu entorno local el siguiente stack tecnológico:
* **Java Development Kit (JDK):** Versión **21** (mínimo requerido).
* **Gestor de dependencias:** Se incluye soporte para Gradle Wrapper (`gradlew`) y Maven Wrapper (`mvnw`), por lo que no es estrictamente necesario instalarlos globalmente.
* **Herramientas de prueba (Opcional):** Postman o `curl` para la verificación de los endpoints REST.

---

## 2. Clonar el Repositorio
Clona tu repositorio público en un directorio local y desplázate a la carpeta raíz:
```bash
git clone https://github.com/eaponte660/spring-petclinic-upgrade.git
cd spring-petclinic-upgrade
```

---

## 3. Configuración del Ambiente
La aplicación utiliza por defecto una base de datos en memoria (H2) integrada para facilitar la ejecución limpia de pruebas y demostraciones sin requerir motores externos complejos.

1. **Variables de entorno:** No se requieren archivos `.env` complejos para el arranque local básico. La configuración se maneja mediante perfiles de Spring Boot.
2. **Perfiles de Spring:** 
   - `local` / `default`: Configurado para H2 Database con consola web habilitada.
3. **Puertos de escucha:** La aplicación se ejecuta por defecto en el puerto **`8080`**.

---

## 4. Compilar, Ejecutar y Verificar

### Usando Gradle (Recomendado)
* **Comando real para compilar el proyecto:**
  ```bash
  ./gradlew clean build
  ```
* **Comando real para ejecutar las pruebas unitarias y de integración:**
  ```bash
  ./gradlew test
  ```
* **Comando real para iniciar la aplicación:**
  ```bash
  ./gradlew bootRun
  ```

### Usando Maven (Alternativa)
* **Comando real para compilar el proyecto:**
  ```bash
  ./mvnw clean package
  ```
* **Comando real para ejecutar las pruebas unitarias y de integración:**
  ```bash
  ./mvnw test
  ```
* **Comando real para iniciar la aplicación:**
  ```bash
  ./mvnw spring-boot:run
  ```

---

## 5. Verificación de Arranque y Documentación Interactiva
Una vez iniciada la aplicación, puedes verificar su estado y acceder a la documentación de las APIs mediante las siguientes URLs:

* **Health Check / Actuator:** `http://localhost:8080/actuator/health`
* **Swagger UI / OpenAPI Documentation:** `http://localhost:8080/swagger-ui/index.html`
* **Consola H2 (Base de datos):** `http://localhost:8080/h2-console` *(URL de JDBC: `jdbc:h2:mem:testdb`, usuario: `sa`, sin contraseña)*

---

## 6. Pruebas Funcionales (Endpoints Principales)

Puedes probar las capacidades migradas utilizando la colección de Postman ubicada en la carpeta `/postman` de este repositorio o mediante peticiones `curl`:

### A. Consultar Veterinarios (con Caché Caffeine activada)
* **Método y Endpoint:** `GET /api/v1/vets`
* **Respuesta esperada:** `200 OK` con la lista de veterinarios y sus especialidades.

### B. Consultar Tipos de Mascotas
* **Método y Endpoint:** `GET /api/v1/pets/types`
* **Respuesta esperada:** `200 OK` con los tipos de mascota disponibles (`dog`, `cat`, etc.).

### C. Gestionar Propietarios y Mascotas
* **Crear Propietario:** `POST /api/v1/owners`
* **Buscar Propietario por ID (Detalle):** `GET /api/v1/owners/{id}` *(Valida optimización de DTOs en colecciones de mascotas)*.
* **Caso de Error (404 Not Found):** `GET /api/v1/owners/9999` *(Valida el correcto funcionamiento del manejador global de excepciones por módulo, retornando un `404` controlado en lugar de un `500`)*.

### D. Registrar y Consultar Visitas
* **Registrar Visita:** `POST /api/v1/visits` (enviando el `petId`, fecha y descripción).
* **Consultar Visitas por Mascota:** `GET /api/v1/pets/{petId}/visits`.

---

## 7. Reproducibilidad desde un Entorno Limpio
Cualquier evaluador puede verificar la solución completa clonando el repositorio, asegurándose de tener instalado Java 21, y ejecutando secuencialmente:
```bash
git clone https://github.com/eaponte660/spring-petclinic-upgrade.git
cd spring-petclinic-upgrade
./gradlew test
./gradlew bootRun
```
