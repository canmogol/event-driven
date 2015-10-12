# event-driven
Event driven multi-threaded services

Jetty+Jersey restful web services added as a service, shade plugin generates a single executable jar, may be executed at command line as "java -jar target/event-driven-1.0-SNAPSHOT.jar", after that check the urls:
- http://localhost:8080/api/login?username=john&password=123
- http://localhost:8080/api/file/application.wadl

or you may try running com.lambstat.test.TestMain under test folder to start the application

Sample client code can be found under "event-driven-client" project under ZMQClient and JerseyClient classes
