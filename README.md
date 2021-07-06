# QueueTracing

This is example application showing how to set traceId for log messages, which could be used in GCP console. To make it work on your machine, please remember to download and set credentials from GCP console, and set following properties in application.properties for both publisher and receiver application:

* `publisher.projectid`, `receiver.projectid` - ID of the project in which your PubSub is created
* `publisher.topicid` - Topic ID of topic to which you will be pushing messages
* `receiver.subscriptionid` - Subscription ID of the subscription which you will use to receive messages

To run application use gradles `bootRun` function. You can run publisher and receiver simultaneously, they are working on different ports - 8080 and 8071.