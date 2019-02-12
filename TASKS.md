Tasks
=====

Create a family-tree service application
----------------------------------------

1. Create a new git repository and share its URL with the team
2. Install ArangoDB
   * Download and extract ArangoDB Community Edition from https://www.arangodb.com/download-major/
     and execute the following command:
     ```
     arangod
     ```
   * You can also start the server via Docker:
     ```
     docker run -p 8529:8529 -e ARANGO_ROOT_PASSWORD= arangodb/arangodb:3.3.x
     ```
   After ArangoDB has been started, you can access its web interface: [http://localhost:8529/](http://localhost:8529/)

3. Create REST endpoints in the application to:
   * Insert new family members and relations among them
   * List cousins for a given family member
   * List grandfathers for a given family member, ordered by `birthDate`
   * List all male/female family members (`gender` can be provided as a parameter)

   The database should contain at least the following properties: `name`, `birthDate`, `gender`

4. Implement unit tests with JGiven
5. Add PMD, Checkstyle and JaCoCo checks to the application

Tips
----
* Integrate ArangoDB with the application: https://github.com/arangodb/arangodb-java-driver
* Checkstyle Maven plugin: https://maven.apache.org/plugins/maven-checkstyle-plugin/
* PMD Maven plugin: https://maven.apache.org/plugins/maven-pmd-plugin/
* JaCoCo Maven plugin: https://www.eclemma.org/jacoco/trunk/doc/maven.html
* JGiven user guide: http://jgiven.org/userguide/
