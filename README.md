# Anypoint Java Client

A Java Client for the Anypoint Platform APIs.

## Installation

Add the dependency in the `pom.xml`

```xml
<dependency>
    <groupId>com.mariocairone.mule</groupId>
    <artifactId>anypoint-java-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

Create an instance using the contructor.

```java
AnypointClient client = new AnypointClientImpl(ANYPOINT_USERNAME,ANYPOINT_PASSWORD);
```
The client uses `JsonIter` to parse the response and return back an instance of `Any`. 

Please refer to the [JsonIter](https://jsoniter.com/) project for more information.

## Troubleshooting

To enable request/response logging set the system property:
```text
-Danypoint.client.debug=true
``` 
