Here’s a **comprehensive set of notes on REST Assured**, structured for easy reference. I’ll cover definitions, setup, usage, examples, and best practices.

---

# **REST Assured Notes**

---

## **1️⃣ What is REST Assured?**

* REST Assured is a **Java-based library** for testing **RESTful web services**.
* It provides a **domain-specific language (DSL)** to write readable and maintainable HTTP request tests.
* Works with **JSON, XML, and HTML** responses.
* Can be used with **JUnit or TestNG** frameworks for automated testing.

**Key Advantages:**

* Simplifies REST API testing
* Supports BDD-style syntax (`given`, `when`, `then`)
* Handles authentication, headers, cookies, and query/path parameters easily
* Can validate response codes, response body, headers, and JSON schema

---

## **2️⃣ Setup**

### **Maven Dependency**

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
    <scope>test</scope>
</dependency>
```

### **Gradle Dependency**

```gradle
testImplementation 'io.rest-assured:rest-assured:5.3.0'
```

### **Optional Dependencies**

* **JSON Path:** For parsing JSON responses
* **XML Path:** For parsing XML responses
* **JUnit/TestNG:** For test execution

---

## **3️⃣ Basic Syntax**

REST Assured uses a **BDD-style structure**:

```java
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

given()           // Pre-conditions: headers, parameters, authentication
    .baseUri("https://api.example.com")
    .header("Content-Type", "application/json")
    .queryParam("page", 2)
.when()           // Action: HTTP method
    .get("/users")
.then()           // Assertions: response validation
    .statusCode(200)
    .body("data.id[0]", equalTo(7));
```

---

## **4️⃣ Common Features**

### **a) HTTP Methods**

* `.get("/endpoint")` – GET request
* `.post("/endpoint")` – POST request
* `.put("/endpoint")` – PUT request
* `.delete("/endpoint")` – DELETE request
* `.patch("/endpoint")` – PATCH request

---

### **b) Request Specifications**

* **Headers**: `.header("Key", "Value")`
* **Query Parameters**: `.queryParam("key", "value")`
* **Path Parameters**: `.pathParam("id", 10)`
* **Body** (JSON/XML): `.body(jsonObject)`

---

### **c) Response Validation**

* **Status Code**: `.statusCode(200)`
* **Body Content**: `.body("key", equalTo("value"))`
* **Header**: `.header("Content-Type", "application/json")`
* **Logging**: `.log().all()` or `.log().body()`

---

### **d) Authentication**

* **Basic Auth**: `.auth().basic("username", "password")`
* **OAuth 2.0**: `.auth().oauth2("token")`

---

### **e) JSON / XML Path**

* Extract values from response:

```java
String name = get("/users/1").then().extract().path("data.name");
```

* Use `JsonPath` for more complex queries:

```java
JsonPath jp = new JsonPath(responseString);
int id = jp.getInt("data[0].id");
```

---

### **f) Assertions**

* Using **Hamcrest Matchers**:

```java
.body("data.id", hasItem(7))
.body("data.name", containsString("John"))
.body("data.size()", greaterThan(0));
```

---

## **5️⃣ Advanced Features**

1. **Reusable Request/Response Specifications**

```java
RequestSpecification reqSpec = new RequestSpecBuilder()
    .setBaseUri("https://api.example.com")
    .addHeader("Content-Type", "application/json")
    .build();

given().spec(reqSpec).when().get("/users").then().statusCode(200);
```

2. **Logging Requests and Responses**

```java
given().log().all()
       .when().get("/users")
       .then().log().body();
```

3. **Data-driven Testing**

* Use TestNG `@DataProvider` or JUnit Parameterized tests with REST Assured requests

4. **Chaining Requests**

* Extract data from one request to use in the next request

```java
int userId = given().get("/users").then().extract().path("data[0].id");
given().pathParam("id", userId).get("/users/{id}");
```

---

## **6️⃣ Practical Examples**

### **GET Request**

```java
given()
    .baseUri("https://reqres.in/api")
.when()
    .get("/users/2")
.then()
    .statusCode(200)
    .body("data.id", equalTo(2))
    .body("data.email", equalTo("janet.weaver@reqres.in"));
```

### **POST Request**

```java
String payload = "{ \"name\": \"John\", \"job\": \"Developer\" }";

given()
    .header("Content-Type", "application/json")
    .body(payload)
.when()
    .post("/users")
.then()
    .statusCode(201)
    .body("name", equalTo("John"))
    .body("job", equalTo("Developer"));
```

---

## **7️⃣ Best Practices**

* **Use `RequestSpecBuilder` and `ResponseSpecBuilder`** for reusable specifications
* **Log requests and responses** during debugging
* **Validate status codes, headers, and body** for thorough testing
* **Use JSON Schema validation** for stricter contract testing
* **Keep test data separate** from test scripts

---

## **8️⃣ Summary**

* REST Assured is a **powerful library for API testing in Java**.
* Uses **BDD-style syntax** for readability.
* Supports **all HTTP methods, authentication, request customization, and response validation**.
* Integrates easily with **JUnit/TestNG** for automated API testing.

---

If you want, I can also create a **cheat sheet with all REST Assured commands and syntax in a single page**, which is super handy for reference during tests.

Do you want me to do that?
