# Getting Started

### External Libraries Used

1. **Spring Boot Starter Web**
    - Purpose: Provides everything needed to build a web application with Spring MVC

2. **Spring Boot Starter Cache**
    - Purpose: Provides caching abstractions and annotations

3. **Caffeine**
    - Purpose: High performance caching library used as the cache implementation

4. **Spring Boot Starter Validation**
    - Purpose: Provides Bean Validation support (JSR-380)

5. **Spring data common & tx**
    - Purpose: Provide @Transactional annotation

6. **SpringDoc OpenAPI UI**
    - Purpose: Automatically generates API documentation (Swagger UI)

6. **slf4j**
   - Purpose: Add logs

7. **Spring Boot Starter Test**
    - Purpose: Provides testing libraries (JUnit, Mockito, etc.)

8. **Gatling**
    - Purpose: High performance load testing tool


### prerequisites
1. **Maven, JDK21**
2. **Docker, k8s cluster** if need deploy to cloud

### How to run
1. Download source codes and run "mvn clean install" to build the project
2. Then make "BankApplication“ as main class to run, localhost:8080 would be directly route to swagger page
3. k8s folder files are for deploy to k8s clusters
4. StressTestRunner can be used for run stress testing 


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/maven-plugin/build-image.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

