# CS122B Homework 1 - The Setup Service

These instructions depend on **IntelliJ IDEA Ultimate**. You can apply for a student license to get this.

#### [Application Settings](#application-settings)

#### [Setup](#setup)

#### [Troubleshooting](#troubleshooting)

#### [Running a Spring Application](#running-a-spring-application)

#### [Running Spring Tests](#running-spring-tests)

## Application Settings

Spring Boot can has a large number of settings that can be set with a file called `application.yml`. \
This file is already provided for you and is placed here for reference.

##### `application.yml`

```yml
spring:
  application:
    name: SetupService

server:
  address: 0.0.0.0
  port: 10010
  error: # These settings are for debugging
    include-exception: true
    include-message: always 

logging:
  file:
    name: ./SetupService.log
```

## Setup
1. In **IntelliJ** go to `File > Open...` and select the template folder.
2. To ensure your Maven dependencies are loaded, go to `View > Tool Windows > Maven`. 
3. A Maven window should appear on the right side of the window.
4. Press the 🔄 button on the top left of the Maven window, a tooltip should read `Reload All Maven Projects`.
   1. If you do not see this follow these steps:
   2. Open your `pom.xml` file.
   3. Right click inside the file. 
   4. Press `+ Add as Maven Project`.
6. Go to `File > Project Structure...`
7. Then press the `Project` tab under `Project Settings` and set your Java SDK.

## Troubleshooting

Here are a couple of ways to clear common errors in IntelliJ:

1. First delete the `target` folder if there is one.
2. Go to `Run > Edit Configurations...` Delete all the configurations
3. Maven window and click `Reload All Maven Projects`.

## Running a Spring Application
1. Go to the Main class (This is the one at the root of the project module) for example:
```java
@SpringBootApplication
public class SetupService
{
    public static void main(String[] args)
    {
        SpringApplication.run(SetupService.class, args);
    }
}
```
2. IntelliJ should add a green arrow to the left of `public class SetupService` press this and click `Run SetupService`
3. If you get a log that ends with something like `Started SetupService in 1.446 seconds (JVM running for 2.146)` then you have Setup everything correctly and you should be ready for the next homework assignment.


## Running Spring Tests
1. Go to the Main Test class (This is the one at the root of the project test module) for example:
```java
@SpringBootTest
@AutoConfigureMockMvc
public class SetupServiceTest
```
2. IntelliJ should add a green arrow to the left of `public class SetupServiceTest` press this and click `Run 'SetupServiceTest'`
3. If you get notification like `Tests passed: 1 of 1 test` then you have Setup everything correctly and you should be ready for the next homework assignment.
4. You can also test individual tests by pressing the green arrow by any `@Test` method. 
    - Make sure if the tests require a database to set up the `Edit configuration templates...` to ensure that the environment variables are added by default if any is needed (like Database username and password)
