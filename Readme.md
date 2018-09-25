# Spark REST Client - Polkichcha
[![Build Status](https://travis-ci.org/dialoglk/spark-rest-client.svg?branch=master)](https://travis-ci.org/dialoglk/spark-rest-client)

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
JOB_QUEUE_SIZE=20
TIME_PER_QUERY_MIN=10
```
  - run the package ```java -jar spark-rest-client-vv.jar```

## Querying
- To run a query, do a HTTP GET to 
```http://your.server.ip.or.hostname:8080/query/databasename/q=base64-ed-query```
The path parameter you give for 'q' should be Base64 encoded before submission

When a valid query is submit, you will receive an ID for reference.

- To retrieve the results of the query, do an HTTP GET to
```http://your.server.ip.or.hostname:8080/status/{ID}```

You will receive the results in as a JSON Object. Observe the following attributes to asses
the result of the query;
```json
"success": [true: whether the query was successful | false: query was not completed]
"timedout": [true: query was timed out | false: query was not timed out]
"data": [your data as JSON Array]

```


## Contributions
May it be an open issue, or your own idea, just fork and give us a pull. We will surely 
merge if its good.

