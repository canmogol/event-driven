# event-driven
Event driven multi-threaded services

Jetty+Jersey restful web services added as a service, shade plugin generates a single executable jar, may be executed at command line as "java -jar target/event-driven-1.0-SNAPSHOT.jar", after that check the urls:
- http://localhost:8080/api/login?username=john&password=123
- http://localhost:8080/api/file/application.wadl

or you may try running com.lambstat.test.Main under test folder to start the application

and also try running com.lambstat.zmq.ZMQClient to test the remote baseEvent dispatch
