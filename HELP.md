# Read Me First
The following was discovered as part of building this project:

* The JVM level was changed from '25' to '21' as the Kotlin version does not support Java 25 yet.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/gradle-plugin/packaging-oci-image.html)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/reference/testing/testcontainers.html#testing.testcontainers)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/reference/web/servlet.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/reference/web/spring-security.html)
* [Testcontainers](https://java.testcontainers.org/)
* [Liquibase Migration](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/how-to/data-initialization.html#howto.data-initialization.migration-tool.liquibase)
* [Quartz Scheduler](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/reference/io/quartz.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.5.7-SNAPSHOT/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:


Please review the tags of the used images and set them to the same as you're running in production.

