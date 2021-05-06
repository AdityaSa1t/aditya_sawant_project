# CS 540 - Spring 2021 - Course Project  
#### Kewal Shah, Aditya Sawant
<br>
#### Description: In this project we present a protoype of a batch processing framework based on Redis. It allows the user to temporarily store its objects and then at a later time make the callback to the function that processes the batch of objects it receives.  
<br>
<br>
#### You can obtain this Git repo using the following command: 
```git clone https://github.com/AdityaSa1t/aditya_sawant_project.git```


## Overview
In a way, batch processing tasks can also be viewed as scheduling tasks with special constraints such as time-windows and queue size which could be configured by the user. Some batching frameworks require a complex set of configurations to impplement batch jobs, so one of the goals of this framework was to present a convenient way to configure batch jobs. There's also the case that some batching frameworks read data only from a given data source such as files, database tables, etc. and don't have the ability to read data directly from objects. Hence, we have developed a soultion for this which requires these objects or their collections to be batched and their execution would be defined by a new or pre-existing function, which can also help developers use existing code. We present a framework that provides flexibility to configure the queues and provides enough freedom to read data and process the batched data without being constrained by the batch processing framework.

<br>
<br>
<br>

## Architecture 
This framework is driven by two configuration files:
1) redis_config.yml is used to manage the configuration for the runnning Redis instance.
2) rbatch_config.yml is used to define the redis lists to hold the objects which need to be batched. It's defined as follows:
```
listName:       #  the list name
    clazz:      #  the fully-qualified domain name of the object that can be stored in this list
    callback:   #  the callback function that needs to be called for thr given list of objects
    size:       #  the maximum number of instances that the list can contain
```

![alt text](https://cdn.discordapp.com/attachments/820506469321277460/839660266072440872/ClassDiagram_1.png)


Out prototype framework consists of 4 core classes:
1) RedisClientUtil - This class is reposible for communicating with Redis. It maintains a single instance of the reis connection object. This class also acts as a proxy to the Redis connection by providing a list of methods to operate over redis.

2) RBatchList and RBatchListBuilder - RBatchList that defines the redis list, it's constraints and the callback which needs to be executed when a certain condition is met. RBatchList can be constructed using RBatchListBuilder. Here we decided to use the builder pattern so that users can create RBatchList objects as per their requirement. We also thought it would be best to use the builder pattern to easily incorporate incremental changes to RBatchList while only changing the the interaction with the RBatchListBuilder if the user wants to incorporate the new features.

3) RBatchProcessor - This class provides APIs to unteract with the framework components.

![alt text](https://cdn.discordapp.com/attachments/820506469321277460/821797322610049104/Fig1.png)

A client communicates with the application via RBatchProcessor to send some input data which is intended to be processed in a batch. 

Then RBatchProcessor would get the corresponding list from its map. In case it isn't found, it calls the RBatchListBuilder to return an instace of the list that can hold the given object and makes an entry for it in the map. Once we have the list, the object is added to it.

In RBatchList, when we add an object, it also checks if it's size or any other constraint has been reached. If this does happen, then it's corresponding callback will be made.

Whenever a callback is made, a new worker thread is spawned to execute it and the queue in Redis would be cleared on successful execution. 
<br>
<br>
<br>


## Technologies used
- Java 8
- Redis
- Maven
- Redisson
- YAML
- JUnit
- log4j
- slf4j

We experimented with Jedis and Redisson to interact with Redis and found that Redisson provides a comprehensive API for working with collections. We found this to be very useful as the collections in Redisson are very similar to Java's default collections. We also considered this would be a good choice in terms of extensibility to support different kinds of abstractions which could be used for batch jobs.

We decided to use YAML to drive our configurations as it is easier to work with compared to JSON or XML files, and it fit our use case the best among the other alternatives.

As for the build tool, we tried to work with SBT. While it allowed us to incorporate all our dependecies, the resulting jar from the compile step had a few issues with Redisson, where it was not able to connect to the Redis server. That is why we decided to switch back to Maven where the resulting jar was able to work properly.


<br>
<br>
<br>


## Instructions 
For the protoype to work, [Redis](https://redis.io/download) needs to be installed on your system and should be running on port 6379, which is the default port for Redis.

Once the installation is done, start the redis server from the command line using the following command:
```redis-start```

Clone this repository using ```git clone https://github.com/AdityaSa1t/aditya_sawant_project.git``` or import this in your  IDE using the URL in the git clone command.

To build the project and run the tests, use the following command on the CLI:
```mvn clean compile install```

To build the project, run the test cases and also run the main function (simulation), use the following command on the CLI:
```mvn clean install exec:java -Dexec.mainClass=com.cs540.rbatch.driver.Main```



