# Kafka Filter Dlt
Example repository for kafka with filter and dlt handler.

The Kafka events send user details when user is added modified or removed.

## Event:
### Event structure:

**Header:** 

`eventType` : `user-added` or `user-modified` or `user-removed`

**Key:** id

**Value:**
```
{
"id" : "23423",
"name" : "test user",
"email" : "test@gmail.com"
}
```
**Docker-compose** file is present in the repository which runs kafka with zookeeper on port 29092. This codebase is 
configured to connect to it and work with it. The **kafka ui** is available on http://localhost:17606 from which we can 
see the topics and send messages.

## EndPoints:

### 1. Get All Kafka Listener details:

**Endpoint:** `GET` http://localhost:8080/kafka/listeners

**Sample response:**
```
{
    "PrioritisedListener": true,
    "NonPrioritisedListener-dlt": true,
    "NonPrioritisedListener": true,
    "PrioritisedListener-dlt": true
}
```
**Curl:**
```
curl --location --request GET 'http://localhost:8080/kafka/listeners'
```

### 2. Start / Stop Listener:

**Endpoint:** `POST` http://localhost:8080/kafka/listeners

**Body:** 
```
{
    "containerId": "NonPrioritisedListener-dlt",
    "operation": "STOP"
}
```

Operation can be `START` or `STOP`.

**Sample response:**
```
{
    "PrioritisedListener": true,
    "NonPrioritisedListener-dlt": false,
    "NonPrioritisedListener": true,
    "PrioritisedListener-dlt": true
}
```
**Curl:**
```
curl --location --request POST 'http://localhost:8080/kafka/listeners' \
--header 'Content-Type: application/json' \
--data-raw '{
    "containerId": "NonPrioritisedListener-dlt",
    "operation": "STOP"
}'
```
**Postman collection** is also present in the repository.

