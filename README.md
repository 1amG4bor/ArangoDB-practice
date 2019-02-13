# Demo Application

How to compile
--------------

    mvn clean package

Usage
-----
* Start server: `mvn spring-boot:run`
* Access Swagger: http://localhost:8080/swagger-ui.html

Running tests
-------------

    mvn test

Generate HTML report
--------------------

    mvn verify or mvn jgiven:report

Reports can be found under: target/jgiven-reports/html/index.html
