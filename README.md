# Vaulted

This is a demo application that shows how to use [Spring Cloud Vault Config](http://cloud.spring.io/spring-cloud-vault/)
to retrieve some secret values from [HashiCorp Vault](https://www.vaultproject.io/) and inject them into the application
context using `@Value` annotation in [DemoApplication.java](src/main/java/com/example/demo/DemoApplication.java) or
placeholders in [application.yml](src/main/resources/application.yml).

In particular, it retrieves some standard Spring properties `spring.datasource.username=demo-user` and
`spring.datasource.password` to configure an H2 datasource.
These properties are defined differently according to active Spring profile(s).

## Starting Vault server

Following command starts a vault server in dev mode with a known initial root token that we can use for dev and tests and listening on http://localhost:8200

~~~
vault server -dev -log-level=INFO -dev-root-token-id=00000000-0000-0000-0000-000000000000
~~~

Before using the CLI to configure the vault, you must set this environment variable:

~~~bash
export VAULT_ADDR=http://localhost:8200
~~~

Add some secret application properties.

~~~bash
# default application, default profile
vault write secret/application spring.datasource.username=default-user spring.datasource.password=default-pass

# demo application, default profile
vault write secret/demo spring.datasource.username=demo-user spring.datasource.password=demo-pass

# demo application, dev profile
vault write secret/demo/dev spring.datasource.username=demo-user-dev spring.datasource.password=demo-pass-dev

# demo application, swagger profile
vault write secret/demo/swagger spring.datasource.username=demo-user-swagger spring.datasource.password=demo-pass-swagger
~~~

## Gotcha

:warning: When writing to a path in Vault, you must write all key/value pairs at once. 

`vault write <existing-path> key1=value1` will blow away any keys other than `key1`.

~~~bash
# Wrong, only spring.datasource.password is stored
vault write secret/application spring.datasource.username=default-user
vault write secret/application spring.datasource.password=default-pass

# Good, the 2 properties are stored
vault write secret/application spring.datasource.username=default-user spring.datasource.password=default-pass
~~~

## Running the application

~~~~
mvnw clean package
~~~~

Without profile, the application prints "default" as `spring.application.name` property is not defined in  [bootstrap.yml](src/main/resources/bootstrap.yml)

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar

##########################
profile(s): null
username: default-user
password: default-pass
other: default-user
Successfully connected to database
##########################
~~~~

With `dev` profile, the application prints "demo-dev" because `spring.application.name` property is defined in  [bootstrap-dev.yml](src/main/resources/bootstrap-dev.yml)

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

##########################
profile(s): dev
username: demo-user-dev
password: demo-pass-dev
other: demo-user-dev
Successfully connected to database
##########################
~~~~

With "dev,swagger" profiles, the application prints "demo-swagger"

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev,swagger

##########################
profile(s): dev,swagger
username: demo-user-swagger
password: demo-pass-swagger
other: demo-user-swagger
Successfully connected to database
##########################
~~~~
