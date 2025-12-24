# Dice Application OpenTelemetry Demo

This is a simple Spring Boot based Dice application created to test and explore OpenTelemetry locally.

This project is a demo / proof of concept and is not intended for production use. The main goal is to provide a ready-made application that can be used to quickly understand OpenTelemetry Java auto-instrumentation in a simple way.

--------------------------------------------------

**PREREQUISITE**

Before running this Dice application, make sure the local OpenTelemetry observability stack is already running.

You must first clone and start the following repository:

https://github.com/saiviswateja/opentelemetry-demo-poc

This setup provides Grafana Alloy, Tempo, Prometheus, and Grafana. The Dice application will send traces to this stack.

--------------------------------------------------

**REQUIREMENTS**

Java 17 or compatible version  
Gradle  
OpenTelemetry Java Agent JAR  
Local observability stack running  

--------------------------------------------------

**BUILD APPLICATION**

Run the following command to build the application:

./gradlew build

The JAR will be generated under:

build/libs/

--------------------------------------------------

**RUN APPLICATION WITH OPENTELEMETRY**

Use the following command to run the Dice application with the OpenTelemetry Java Agent:

java \
  -javaagent:/Users/srama/Documents/dummy_projects/DiceApplicaton/opentelemetry-javaagent.jar \
  -Dotel.service.name=diceapplication \
  -Dotel.javaagent.debug=true \
  -Dotel.metrics.exporter=none \
  -Dotel.logs.exporter=none \
  -Dmanh.instrumentation.type.prefixes=com.diceapplication.DiceApplicaton,com.hazelcast.map.impl \
  -jar build/libs/DiceApplicaton-0.0.1-SNAPSHOT.jar

--------------------------------------------------

**WHAT THIS SETUP DOES**

The OpenTelemetry Java Agent is attached to the application.  
Traces are automatically generated without code changes.  
Metrics and logs are disabled to keep the demo simple.  
Traces are exported to the local observability stack.  

--------------------------------------------------

**EXPECTED RESULT**

The application starts successfully.  
Traces are generated when APIs are called.  
Traces are visible in Grafana using the Tempo datasource.  

--------------------------------------------------

**WHO SHOULD USE THIS PROJECT**

Developers learning OpenTelemetry  
Engineers creating demos or PoCs  
Anyone wanting a simple Java tracing example  

--------------------------------------------------

**NOTE**

This project is meant only for local testing and learning.  
It is not recommended for production use.

--------------------------------------------------
