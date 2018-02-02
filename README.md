# Telegram bot to call Ryanair API

This purpose of this project is create a bot that answer to some instruction about flights information.

Examples of questions:
- Give me the CONNECTIONS between MAD and DUB
- Give me FLIGHTS between MAD and WRO

## Architecture 
The idea is to use seven services using the Microservices paradigm.

- ryanair-eureka-server       : The Service Discovery
- ryanair-bot-config-server   : The configuration server will access to ryanair-bot-config-repository
- ryanair-bot-travelgram			: This will use the Telegram API to create the bot
- ryanair-interconnections    : Service who will deliver basis flights information
- command-service             : Service use as dispatcher to call other Microservices
- ryanair-fares               : Service will deliver fares information
- ryanair-sheduled-flights    : Services who will deliver scheduled flights info

## Configuration

### Host Modification
It's necessary modify the host file in your machine. Adding three new hostname. In order to replicate a three replicas of the Services Discovery.

sudo nano /etc/hosts

add the following names:
127.0.0.1       eureka-primary
127.0.0.1       eureka-secondary
127.0.0.1       eureka-tertiary

## Execution

### Run the Configuration Server
In the application.yml modify the uri and the searchPaths in order to point your repository.

`server:
  port: 9000
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-user/git-name-repo
          searchPaths: folder-if-exists
          username: user-if-private
          password: password-if-private`

Then execute `mvn spring-boor:run`

Check if is ok calling the server. You should see the application.yml file:
http://localhost:9000/ryanair-bot-config-server/default


### Run Eureka Services
After compile the project, run three instances of the server.

java -jar -Dspring.profiles.active=primary eureka-server-0.0.1-SNAPSHOT.jar
java -jar -Dspring.profiles.active=secondary eureka-server-0.0.1-SNAPSHOT.jar
java -jar -Dspring.profiles.active=tertiary eureka-server-0.0.1-SNAPSHOT.jar

### Run the rest of the Services
In the root directory of every project execute:

`mvn clean spring-boor:run`
