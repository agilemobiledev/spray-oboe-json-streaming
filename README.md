# Spray Oboe JSON Streaming

Example of streaming JSON objects using [spray.io](http://spray.io/) on the backend and [oboe.js](http://oboejs.com) on the frontend to handle the events. Events are used to update a gauge provided by [JustGauge](http://justgage.com/).

## Run

```
sbt run
```

To test the stream:

```
curl http://localhost:8080/stream/ben
curl -H 'Accept-Encoding: gzip' http://localhost:8080/stream/ben
```

Open `index.html` whilst the server is running to view the gauge being updated with the stream events.