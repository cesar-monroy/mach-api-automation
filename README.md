# MACH API Framework

🚀 Framework de testing de APIs y librería reutilizable para consumir servicios REST usando RestAssured y Java.

## 📋 Descripción

Este proyecto cumple dos propósitos principales:

1. **Framework de Testing de APIs**: Proporciona clases base y utilidades para escribir tests de APIs de manera sencilla y mantenible.
2. **Librería Reutilizable**: Puede ser usado como dependencia en otros proyectos para consumir servicios REST de forma simple y consistente.

### 🎯 Estructura Similar a GenericRestClient

El `RestClient` está estructurado de manera similar a `GenericRestClient` del proyecto MACH-CORE-AUTOMATION para mantener consistencia y facilitar la migración entre proyectos. Los métodos principales (`runSimpleWebService`, `runWebServiceWithBody`) siguen el mismo patrón, lo que permite una curva de aprendizaje más suave.

## ✨ Características

- ✅ **Cliente REST reutilizable** (`RestClient`) para consumir servicios
- ✅ **Clase base para testing** (`BaseApiTest`) con TestNG
- ✅ **Configuración flexible** mediante propiedades, variables de entorno o system properties
- ✅ **Integración con Allure** para reportes de pruebas
- ✅ **Utilidades para extracción de datos** de respuestas JSON
- ✅ **Soporte para múltiples métodos HTTP** (GET, POST, PUT, PATCH, DELETE)
- ✅ **Autenticación** (Bearer Token, Basic Auth)
- ✅ **Logging automático** de requests y responses

## 📦 Instalación

### Como Dependencia Maven

Agrega la dependencia a tu `pom.xml`:

```xml
<dependency>
    <groupId>com.mach.api</groupId>
    <artifactId>mach-api-framework</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Instalación Local

Para instalar la librería en tu repositorio local de Maven:

```bash
mvn clean install
```

## 🚀 Uso

### 1. Como Framework de Testing

#### Crear una clase de test

```java
package com.mach.api;

import com.mach.api.test.BaseApiTest;
import io.restassured.http.Method;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class MyApiTest extends BaseApiTest {

    @Test
    public void testGetResource() {
        // Ejecutar GET request usando runSimpleWebService
        runSimpleWebService(Method.GET, "/resource/1")
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", notNullValue());
    }

    @Test
    public void testCreateResource() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "John Doe");
        requestBody.put("email", "john@example.com");

        // Ejecutar POST request usando runWebServiceWithBody
        runWebServiceWithBody(Method.POST, "/resource", requestBody)
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("John Doe"));
    }

    @Test
    public void testGetResourceWithQueryParams() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);
        queryParams.put("limit", 10);

        runSimpleWebService(Method.GET, null, queryParams, "/resource")
                .statusCode(200);
    }
}
```

#### Configuración

Crea un archivo `api.properties` en `src/main/resources/`:

```properties
api.base.uri=https://api.example.com
api.base.path=/api/v1
api.bearer.token=your-token-here
```

O usa variables de entorno:

```bash
export API_BASE_URI=https://api.example.com
export API_BEARER_TOKEN=your-token-here
```

### 2. Como Librería Reutilizable

#### Uso básico

```java
import com.mach.api.client.RestClient;
import com.mach.api.util.ResponseExtractor;
import io.restassured.http.Method;
import io.restassured.response.ValidatableResponse;

// Crear cliente con autenticación
RestClient client = new RestClient("https://api.example.com", "/v1", "your-token-here");

// Ejecutar GET request
ValidatableResponse response = client.runSimpleWebService(Method.GET, "/users/1");

// Extraer datos del response
int statusCode = response.extract().statusCode();
String responseBody = response.extract().body().asString();
String userName = response.extract().body().jsonPath().getString("name");
```

#### Ejemplo completo

```java
// 1. Crear cliente con diferentes tipos de autenticación
RestClient client = new RestClient("https://api.example.com", "/v1");
RestClient clientWithToken = new RestClient("https://api.example.com", "/v1", "token");
RestClient clientWithAuth = new RestClient("https://api.example.com", "/v1", "user", "pass");

// 2. Ejecutar requests simples
ValidatableResponse getResponse = client.runSimpleWebService(Method.GET, "/users/1");

// 3. Ejecutar con query parameters
Map<String, Object> queryParams = new HashMap<>();
queryParams.put("page", 1);
queryParams.put("limit", 10);
ValidatableResponse listResponse = client.runSimpleWebService(
    Method.GET, null, queryParams, "/users");

// 4. Ejecutar con path parameters
Map<String, Object> pathParams = new HashMap<>();
pathParams.put("id", 1);
ValidatableResponse userResponse = client.runSimpleWebService(
    Method.GET, null, "/users/{id}", pathParams);

// 5. Ejecutar con body
Map<String, Object> requestBody = new HashMap<>();
requestBody.put("name", "John Doe");
requestBody.put("email", "john@example.com");
ValidatableResponse createResponse = client.runWebServiceWithBody(
    Method.POST, "/users", requestBody);

// 6. Ejecutar con body y headers personalizados
Map<String, Object> headers = new HashMap<>();
headers.put("X-Custom-Header", "value");
ValidatableResponse createWithHeaders = client.runWebServiceWithBody(
    Method.POST, headers, "/users", requestBody);

// 7. Ejecutar con body y path parameters
Map<String, Object> updateBody = new HashMap<>();
updateBody.put("name", "Jane Doe");
ValidatableResponse updateResponse = client.runWebServiceWithBodyAndPathParams(
    Method.PUT, null, "/users/{id}", pathParams, updateBody);

// 8. Obtener última respuesta desde SessionStorage
Response lastResponse = RestClient.getLastResponse();
if (lastResponse != null) {
    String responseBody = ResponseExtractor.extractJsonPath(lastResponse, "name");
}
```

## 📚 API Reference

### RestClient

Cliente principal para consumir servicios REST. Estructura similar a GenericRestClient para consistencia.

#### Constructores

```java
// Constructor por defecto
RestClient client = new RestClient();

// Con base URI y path
RestClient client = new RestClient("https://api.example.com", "/api/v1");

// Con base URI, path y Bearer token
RestClient client = new RestClient("https://api.example.com", "/api/v1", "token");

// Con base URI, path y Basic Auth
RestClient client = new RestClient("https://api.example.com", "/api/v1", "username", "password");
```

#### Métodos Principales

```java
// Ejecutar servicio simple (sin body) - GET, DELETE, etc.
ValidatableResponse response = client.runSimpleWebService(Method.GET, "/users/1");

// Ejecutar servicio simple con headers
ValidatableResponse response = client.runSimpleWebService(Method.GET, headers, "/users/1");

// Ejecutar servicio simple con headers y query parameters
ValidatableResponse response = client.runSimpleWebService(Method.GET, headers, queryParams, "/users");

// Ejecutar servicio simple con headers y path parameters
ValidatableResponse response = client.runSimpleWebService(Method.GET, headers, "/users/{id}", pathParams);

// Ejecutar servicio con body - POST, PUT, PATCH
ValidatableResponse response = client.runWebServiceWithBody(Method.POST, "/users", userObject);

// Ejecutar servicio con body y headers
ValidatableResponse response = client.runWebServiceWithBody(Method.POST, headers, "/users", userObject);

// Ejecutar servicio con body y path parameters
ValidatableResponse response = client.runWebServiceWithBodyAndPathParams(
    Method.PUT, headers, "/users/{id}", pathParams, userObject);
```

### ResponseExtractor

Utilidades para extraer datos de respuestas.

```java
// Como objeto (usando tu clase POJO)
MyModel model = ResponseExtractor.asObject(response, MyModel.class);

// JSON Path
String value = ResponseExtractor.extractJsonPath(response, "path.to.value");
```

### BaseApiTest

Clase base para tests de API. Usa la misma estructura que GenericRestClient.

```java
import io.restassured.http.Method;

public class MyApiTest extends BaseApiTest {
    
    @Test
    public void myTest() {
        // Request simple
        runSimpleWebService(Method.GET, "/endpoint")
                .statusCode(200);
        
        // Request con body
        runWebServiceWithBody(Method.POST, "/endpoint", requestObject)
                .statusCode(201);
        
        // Request con query parameters
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("page", 1);
        runSimpleWebService(Method.GET, null, queryParams, "/endpoint")
                .statusCode(200);
    }
}
```

## ⚙️ Configuración

### Prioridad de Configuración

1. **System Properties** (máxima prioridad)
2. **Variables de Entorno**
3. **Archivo `api.properties`**

### Variables de Entorno

```bash
export API_BASE_URI=https://api.example.com
export API_BASE_PATH=/api/v1
export API_BEARER_TOKEN=your-token
export API_USERNAME=username
export API_PASSWORD=password
```

### System Properties

```bash
java -Dapi.base.uri=https://api.example.com -Dapi.bearer.token=token ...
```

### Archivo de Propiedades

Crea `src/main/resources/api.properties`:

```properties
api.base.uri=https://api.example.com
api.base.path=/api/v1
api.bearer.token=your-token
api.timeout=30000
```

## 📁 Estructura del Proyecto

```
mach-api-framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mach/
│   │   │           └── api/
│   │   │               ├── client/          # Cliente REST reutilizable
│   │   │               ├── test/            # Clases base para testing
│   │   │               ├── config/          # Configuración
│   │   │               ├── util/            # Utilidades
│   │   │               └── account/          # Servicio Account
│   │   │                   ├── AccountApiClient.java
│   │   │                   └── model/        # Modelos del servicio
│   │   └── resources/
│   │       └── api.properties.example
│   └── test/
│       └── java/                            # Tests de ejemplo
├── pom.xml
└── README.md
```

## 🔧 Dependencias Principales

- **RestAssured**: Cliente HTTP para testing y consumo de APIs
- **TestNG**: Framework de testing
- **Allure**: Reportes de pruebas
- **Gson/Jackson**: Procesamiento JSON
- **SLF4J**: Logging

## 📝 Ejemplos

Ver los ejemplos en:
- `src/test/java/com/mach/api/account/AccountApiTest.java` - Tests para creación de usuarios con Account API
- `src/main/java/com/mach/api/account/AccountApiClient.java` - Cliente para Account API

### Uso: Account API - Creación de Usuarios

Ejemplo de uso con el endpoint Account API para crear nuevos usuarios:

#### Usando AccountApiClient (Recomendado)

```java
import com.mach.api.account.AccountApiClient;
import com.mach.api.account.model.AccountRequest;
import com.mach.api.account.model.AccountAction;

// 1. Crear cliente con Bearer token
AccountApiClient accountClient = new AccountApiClient("your-bearer-token");

// 2. Crear usuario con configuración por defecto
ValidatableResponse response = accountClient.createAccount();
response.statusCode(200);

// 3. Crear usuario con argumentos personalizados
Map<String, Object> args = new HashMap<>();
args.put("email", "user@example.com");
args.put("validateEmail", true);
ValidatableResponse response2 = accountClient.createAccount(args);

// 4. Crear usuario con request completo
AccountRequest request = new AccountRequest();
AccountAction action = new AccountAction();
action.setName("createAccountAction");
action.setArgs(new HashMap<>());
request.setActions(new AccountAction[]{action});

ValidatableResponse response3 = accountClient.createAccount(request);
```

#### Usando RestClient directamente

```java
// 1. Crear cliente con Bearer token
RestClient client = new RestClient(
    "https://account-faker-api-staging-stg.soymach.com", 
    "", 
    "your-bearer-token"
);

// 2. Preparar request con POJO
AccountRequest request = new AccountRequest();
AccountAction action = new AccountAction();
action.setName("createAccountAction");
action.setArgs(new HashMap<>());
request.setActions(new AccountAction[]{action});

// 3. Preparar headers
Map<String, Object> headers = new HashMap<>();
headers.put("X-Api-Key", "ii92khXeY#q<t38W!8m{");
headers.put("Content-Type", "application/json");

// 4. Ejecutar POST request
ValidatableResponse response = client.runWebServiceWithBody(
    Method.POST, headers, "/account", request);

// 5. Validar respuesta
response.statusCode(200);
```

## 🤝 Contribución

Para contribuir a este proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es propiedad de MACH y está destinado para uso interno.

## 📞 Soporte

Para soporte o preguntas, contacta al equipo de automatización.
