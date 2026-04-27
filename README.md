# Smart Campus

This repository contains the implementation of the Smart Campus for the ClientServer Architectures coursework.

## API Overview

The Smart Campus API is a RESTful API built using JAX-RS framework. It provides endpoints for managing rooms, sensors, and sensor readings within a smart campus environment.
The API follows the principles of REST and using HTTP methods for performing CRUD operations on resources.

## Build Instructions

To build and run the Smart Campus API project, follow these steps:

## Clone the repository
- git clone https://github.com/Umahansi/SmartCampus.git
  
### Using NetBeans

1. Open NetBeans IDE and go to "File" > "Open Project".
2. Navigate to the project directory and select the `SmartCampus` project.
3. Right-click on the project in the NetBeans Projects view and select "Clean and Build" to compile the project and create the WAR (Web Archive) file.
4. After the build is successful, right-click on the project again and select "Run" or press the "F6" key.
5. NetBeans will start the server (Apache Tomcat) and deploy the application.
6. Once the application is deployed, you can access the API endpoints using a tool like Postman or cURL.

### Using Command Line

1. Open a terminal or command prompt and navigate to the project's root directory.
- cd SmartCampus
3. Make sure you have Maven installed on your system. If not, please install Maven first.
- https://maven.apache.org/download.cgi
4. Run the following command to build the project:
- mvn clean install
4. After the build is successful, navigate to the `target` directory:
-cd target
5. Look for the WAR file named `SmartCampus.war` (or similar) in the `target` directory.

6. Deploy the WAR file to a servlet container or application server of your choice (Apache Tomcat).
- For Tomcat, copy the WAR file to the `webapps` directory of your Tomcat installation.
- Start the Tomcat server if it's not already running. - eg:- `http://localhost:8080/SmartCampus/api/v1`
  
7. Once the application is deployed, you can access the API endpoints using a tool like Postman or cURL.


## API Endpoints
- `GET /api/v1`: Retrieve API metadata and available resource collections
- `GET /api/v1/rooms`: Retrieve a list of all rooms
- `POST /api/v1/rooms`: Create a new room
- `GET /api/v1/rooms/{roomId}`: Retrieve details of a specific room
- `DELETE /api/v1/rooms/{roomId}`: Delete a room
- `GET /api/v1/sensors`: Retrieve a list of all sensors
- `POST /api/v1/sensors`: Create a new sensor
- `GET /api/v1/sensors/{sensorId}/readings`: Retrieve sensor readings for a specific sensor
- `POST /api/v1/sensors/{sensorId}/readings`: Create a new sensor reading

## Sample cURL Commands

Here are some sample cURL commands to interact with-

1. Retrieve API metadata
- `curl -X GET http://localhost:8080/SmartCampus/api/v1`
2. Create a new room 
- `curl -X POST http://localhost:8080/SmartCampus/api/v1/rooms ^
-H "Content-Type: application/json" ^
-d "{\"id\":\"R001\",\"name\":\"Room 1\",\"capacity\":10}"`
3. Create a new sensor
- `curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors ^
-H "Content-Type: application/json" ^
-d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0,\"roomId\":\"R001\"}"`
3. Retrieve a list of all sensors
- `curl -X GET http://localhost:8080/SmartCampus/api/v1/sensors`
4. Create a new sensor reading
- `curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-001/readings ^
-H "Content-Type: application/json" ^
-d "{\"id\":\"reading-001\",\"timestamp\":1712000000000,\"value\":23.5}"`
5. Retrieve sensor readings for a specific sensor
- `curl -X GET http://localhost:8080/SmartCampus/api/v1/sensors/TEMP-001/readings`

---
## Coursework Report

### Part 1: Service Architecture & Setup

#### Question 1 : In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

By default, JAX-RS resource classes are treated as singleton by the runtime. This means that a single instance of the resource class is created and shared across multiple requests. This architectural decision has implications on managing and synchronizing in memory data structures within the resource class. Because a new object is created for each request instance variables inside the resource classes do not continue between requests. Therefore, shared data such as HashMap, ArrayList must be stored static structures or services classes.
Since multiple requests may occur simultaneously, multiple threads may access the same shared collections and this may cause race conditions or data inconsistency. In order to mitigate this we should use thread safe collections such as ConcurrentHashMap and synchronizations mechanisms. This ensures data is not lost or corrupted when many requests modify the same resource.

#### Question 2 : Why is the provision of “Hypermedia” (links and navigation within responses) considering a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

Hypermedia known as HATEOAS means that an API response includes links to related resources and actions. The API returns navigational links that guide the client on what it can do next instead of returning only data. HATEOAS is important because it’s a self-discoverable API where clients can navigate the API dynamically without knowing all endpoints beforehand. Developers do not need to memorize every endpoint because responses provide navigation links. Static documentation requires developers to hard code endpoints URLs. With hypermedia the API itself tells the client how to interact with it, making system more flexible and scalable.


### Part 2: Room Management

#### Question 1 : When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

When returning a list of rooms, there are tradeoffs to consider between returning only IDs versus returning the full room objects.
Returning only ids benefits, since it only sending the room IDs required less data to be transmitted over the network. This results in faster response times and lower bandwidth consumption. But if the client needs more information about each room this action might lead to the need of more additional requests. Meanwhile returning full room objects the client can receive all the necessary information about each room in a single response, without needing additional requests. But this might increase the network bandwidth because sending the full room objects requires more data to be transmitted over the network and if the client doesn’t need all the room details upfront this action might be unnecessary.

#### Question 2 : Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple time.

Yes, the DELETE operation is idempotent in my implementation. Idempotence simply means that multiple identical requests should have the same effect as a single request, without causing any unintended side effects.
In the case of deleting a room, if a client sends the exact same DELETE request multiple times for a room, first delete request checks if the room exists and no linked sensors and successfully deletes the room and server responds with a 200 OK with a message.
Second DELETE request, since the room has already been deleted by the first request the server responds with a 409 Not Found Status code. No further changes occur in the system and this behavior ensures that deleted operation is idempotent.


### Part 3: Sensor Operations & Linking

#### Question 1 : We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

Using the @Consumes (MediaType.APPLICATION_JSON) annotation specifies that the method expects the request payload to be in JSON format. If a client attempts to send data in a different format JAX-RS handles the mismatch by sending a unsupported media type response. If the client sends request with a media type not supported by the server, JAX-RS automatically returns a HTTP response with the 415 status code. This response indicates that the server cannot process the request due to the incompatible media type. If the client sends a request that is supported by server but does not match the expected JSON format, JAX-RS attempts to parse the request payload according to the specified media type. Since the payload is not in the expected JSON format the parsing fails. Returns a HTTP response with code 400 bad request.
If the request parsing fails due to an incompatible media type JAX-RS throws an exception such as MessageBodyProviderNotFoundException. 


#### Question 2 : You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

In the implementation filtering of sensors is done using @QueryParam, where the type parameter is provided as a query parameter in the URL. This approach is generally considered superior compared to an alternative design where type is part of the URL.
Query parameters allow for easy combination of multiple filtering criteria using standard URL query string conventions and allow for easier API versioning and evolution as clients can leverage the same base URL while adapting to changes in filer parameters.

### Part 4: Deep Nesting with Sub-Resources

#### Question : Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive con troller class?

The sub resource locator pattern enhances API design by breaking down the logic for nested resources into individual classes rather than having a single controller. Here, a primary resource finds and returns a different resource class to handle all operations for the sub-resource like managing the sensors/{id}/readings/ path in a separate SensorReadingResource class.
This pattern reduces complexity in large APIs as each class is dedicated to a resource. Rather than a single large controller that handles numerous nested path the code is broken down into smaller resource classes. This makes it simpler to comprehend, modify and add to.
Dividing resources enhances code clarity. Changes can be made to different resource classes without impacting other parts of the API, and a change to a sub-resource does not require a change to the controller. This approach facilitates scalability as the API expands, without making the code unwieldy

### Part 5: Advanced Error Handling, Exception Mapping & Logging

#### Question 1 : Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

1.	HTTP 422 is defined as “the server understands the content type of the request entity and the syntax of the request entry is correct but was unable to process the contained instructions. This specifically indicates that the request payload is syntactically correct but cannot process due to logical error or missing references. HTTP 404 on the other hand typically used to indicate that a requested resource cannot found.
2.	When encountering 422 status code developers can easily identify that the issue is related to the request payload and its content. This narrows down the scope of investigation to the payload itself rather than suggesting a general “not found” error.

However, using HTTP 422 for missing references inside a valid payload is a widely accepted practice and aligns with the principles of RESTful API design.

#### Question 2 : From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing internal Java stack traces to external API consumers expose some risks. Stack traces reveal implementation details like package names, class names providing insights into the technology stack. Attackers can use stack traces to understand internal logic, identify sensitive operations and target specific areas for exploitation. Furthermore, the attackers can reverse engineer the API using stack trace information to identify weakness such as improper input validation.

#### Question 3 : Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single re source method?

Filters allow separation of concerns, keeping resource methods focused on core logic while centralizing logging functionality in single location. They are reusable components that can be applied across multiple resource promoting code reuse and code duplication. Filters ensure consistency and uniformity in logging behavior throughout the API and provide flexibility, allowing customization of logging behavior without modifying resource methods.

