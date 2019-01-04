var initTracer = require("jaeger-client").initTracer;

// See schema https://github.com/jaegertracing/jaeger-client-node/blob/master/src/configuration.js#L37
var config = {
  serviceName: "first-level-node-api",
  reporter: {
    logSpans: true,
    agentHost: "172.18.0.4",
    agentPort: 6832
  },
  sampler: {
    type: "probabilistic",
    param: 1.0
  }
};
var options = {
  tags: {
    "first-level-node-api.version": "1.0.0"
  }
};
var tracer = initTracer(config, options);

// rest of your code
const { FORMAT_HTTP_HEADERS } = require("opentracing");
const express = require("express");
const request = require("request");
const consul = require("consul")({
  host: "172.18.0.2"
});

const app = express();

//SETUP AS CONSUL CLIENT
let known_data_instances = [];

const watcher = consul.watch({
  method: consul.health.service,
  options: {
    service: "node-api",
    passing: true
  }
});

watcher.on("change", data => {
  console.log("listening to changes", data);
  known_data_instances = [];
  data.forEach(entry => {
    known_data_instances.push(entry);
  });
});

app.route("/foo").get(function(req, res) {
  if (known_data_instances.length) {
    const span = tracer.startSpan("http_request");
    span.log({ calling: "/foo" });
    const options = {
      url: `http://${known_data_instances[0].Service.Address}:${
        known_data_instances[0].Service.Port
      }/foo`,
      headers: req.headers
    };
    span.log({ calling: options.url });

    tracer.inject(span, FORMAT_HTTP_HEADERS, options.headers);

    request(options, (err, result, body) => {
      if (err) {
        throw new Error(err);
      }
      span.log({ event: "data_received", data: body });
      console.log(body);
      res.json(body);
      span.finish();
    });
  } else {
    res.json("Couldn't find the NODE-API service 👎");
    console.log("Couldn't find the NODE-API service 👎");
  }
});

app.listen(3001);
