## widget-service

Spring MVC @RestController based HTTP REST APIs for storing & retrieving Widget objects. 

### Application Setup
* Java 11 (Tested with 11.0.8 AdoptOpenJDK 64 Bit) 
* Apache Maven 3.6.3 
  * Maven wrapper is included in repo

#### How to run
* Clone the Github repository 
* From inside the project directory 
  * Run `./mvnw spring-boot:run` OR `mvn spring-boot:run` to start the Server 
  * Logs are appended onto Console & in the `widgetservice.log` file under `logs` directory.
  
* Tests can be run via ` ./mvnw test`
* Coverage report can be generated via `./mvnw jacoco:report` 
  * Coverage report then can be found under `target/site/index.html` 


#### Sample curl requests

* **LIST** widgets with pageSize=2 and startKey=-1. 
  `startKey` is the zIndex to list the widgets from. This endpoint returns a widget list _`sorted by zIndex`_.
  Response contains `nextStartKey` which can then be used in subsequent request for next page. 
  
```
curl --location --request GET 'http://localhost:8080/api/widgets/?pageSize=2&startKey=-1' \
--header 'Content-Type: application/json' 
```
* **FILTER** Widgets by passing a rectangle coordinates. Here `(x1,y1)` represents the bottomLeft & `(x2,y2)` represents the topRight corners of rectangle.
  For finding an overlap with a Widget I've assumed that `(xCoordinate,yCoordinate)` represents the bottomLeft corner and `(xCoordinate+width,yCoordinate+height)` represents the topRight corner of the Widget.
  
```
curl --location --request GET 'http://localhost:8080/api/widgets/filter?x1=2&y1=3&x2=4&y2=5' \
--header 'Content-Type: application/json' \
```

* **CREATE** a new widget by providing attributes(x,y,width,height,z)
```
curl --location --request POST 'http://localhost:8080/api/widgets/' \
--header 'Content-Type: application/json' \
--data-raw '{"x":2,"y":3,"width":1,"height":2}'
```

* **UPDATE** widget attributes for a particular widget by Id.
If `z` is not specified it not updated for the given Widget. 
```
curl --location --request PATCH 'http://localhost:8080/api/widgets/ef31179e-016f-4dfe-991f-9aeaf2590e05' \
--header 'Content-Type: application/json' \
--data-raw '{"x":2,"y":3,"width":1,"z":2}'
```

* **GET** a widget by Id
```
curl --location --request GET 'http://localhost:8080/api/widgets/b3ed3b1f-6509-4b32-9b26-ae5a53635c6d' \
--header 'Content-Type: application/json'
```

* **DELETE** a widget by Id

```
curl --location --request DELETE 'http://localhost:8080/api/widgets/dfb4b4c7-617e-4e3c-977c-0592edd2ee32' \
--header 'Content-Type: application/json' 
```


### Improvements
* Response to contain links to resources.
  * specially pagination endpoint to return nextPage link.
  * Use a RepresentationModelAssembler
* Expose Docs for the endpoints.  
* Improve test coverage, current coverage is around 67%. 
    * Repository specific tests are missing. 
    * Filter & Pagination specific E2E test are missing.
* Repository can be refactored with specific store being outside.
* Extend/Implement WidgetRepository from/with Spring Data JPA. 


Cheers
