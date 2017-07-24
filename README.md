# Vaulted

This is a demo application that shows how to use [Spring Cloud Vault Config](http://cloud.spring.io/spring-cloud-vault/) to retrieve some secret values from [HashiCorp Vault](https://www.vaultproject.io/) and inject them into the application context using `@Value` annotation in [DemoApplication.java](src/main/java/com/example/demo/DemoApplication.java) or placeholders in [application.yml](src/main/resources/application.yml).

## Starting Vault server

Following command starts a vault server in dev mode with a known initial root token that we can use for dev and tests and listening on http://localhost:8200

~~~
vault/vault server -dev -dev-root-token-id=00000000-0000-0000-0000-000000000000
~~~

Before using the CLI to configure the vault, you must set this environment variable:

~~~bash
export VAULT_ADDR=http://localhost:8200
~~~

Add some secrets application properties

~~~bash
# default application, default profile
vault/vault write secret/application spring.datasource.username=default

# vaulted application, default profile
vault/vault write secret/vaulted spring.datasource.username=vaulted

# vaulted application, dev profile
vault/vault write secret/vaulted/dev spring.datasource.username=vaulted-dev

# vaulted application, swagger profile
vault/vault write secret/vaulted/swagger spring.datasource.username=vaulted-swagger
~~~


## Running the application

~~~~
mvnw clean package
~~~~

Without profile, the application prints "default" as `spring.application.name` property is not defined in  [bootstrap.yml](src/main/resources/bootstrap.yml)

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar

##########################
username: default
other: default
##########################
~~~~

With `dev` profile, the application prints "vaulted-dev" because `spring.application.name` property is defined in  [bootstrap-dev.yml](src/main/resources/bootstrap-dev.yml)

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

##########################
username: vaulted-dev
other: vaulted-dev
##########################
~~~~

With "dev,swagger" profiles, the application prints "vaulted-swagger"

~~~~
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev,swagger

##########################
username: vaulted-swagger
other: vaulted-swagger
##########################
~~~~
