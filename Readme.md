# Spark REST Client - Polkichcha

Polkichcha is a lightweight REST client to connect your spark cluster via a thrift server. Just submit the query in your HTTP request and Polkichcha will execute it on the thrift JDBC and present the results.

Polkichcha uses the hive JDBC libraries with the [WSO2 MSF4J](https://github.com/wso2/msf4j) microservices framework.
## Building

  - Clone the repo
  - do a ```mvn package```

## Running
  - Create the config file that has the properties to connect to Thrift
```
THRIFT_IP=host.or.ip.here
THRIFT_PORT=8089
THRIFT_USERNAME=
THRIFT_PASSWORD=
```
  - run the package ```java -jar spark-rest-client-vv.jar```

## Contributions
May it be an open issue, or your own idea, just fork and give us a pull. We will surely merge if its good.

