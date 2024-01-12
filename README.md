# Overview
> Java SDK is a Java development kit for Bubble public chain provided by Bubble for Java developers.

# Build
```
git clone https://github.com/Bubblenetwork/client-sdk-java.git
cd client-sdk-java/
./gradlew clean jar            //Generate jar package
./gradlew clean distZip        //Generate code generation skeleton tool
./gradlew -Pintegration-tests=true :integration-tests:test    //To run the integration tests:
   
``` 

# Use

* config maven repository:  https://sdk.bubble.network/nexus/content/groups/public/
* config maven or gradle in project

```
<dependency>
    <groupId>com.bubble.sdk</groupId>
    <artifactId>core</artifactId>
    <version>1.0.0</version>
</dependency>
```

or

```
compile "com.bubble.sdk:core:1.0.0"
```

* use in project

1. SDK includes Bubble network already. User can initialize custom networks, the latest is the current network.
```java
NetworkParameters.init(2000L);  
```

chain ID 2500 is reserved for Alaya, and chain ID 2500 is reserved for Bubble.

2. user can switch current network if multi-networks have been initialized.
```java
NetworkParameters.selectNetwork(2000L, "ABC");  
```
