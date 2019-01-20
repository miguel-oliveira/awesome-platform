# awesome-platform

multi-cloud platform playground

## Findings

### Service Discovery and Binding

- with auto health-checks, it becomes very easy to register services (check "Consul Health Checks" below)

### Distributed Tracing

- instrumentation is laborious but can be abstracted.
- **TODO**: The Jaeger Spring Boot client gives **all** instrumentation for free. Need to test it.
- integration of the in-built tracing information with something like prometheus can be explored. The consul telemetry documentation mentions the following with this regard: "Additionally, if the telemetry configuration options are provided, the telemetry information will be streamed to a statsite or statsd server where it can be aggregated and flushed to Graphite or any other metrics store. This information can also be viewed with the metrics endpoint in JSON format or using Prometheus format."
- also refer consul.http.<verb>.<path> under the Metrics Reference section in the consul telemetry documentation.

### Caching with Redis

- **TODO**: need to test performance gain from caching (vs read from memory and vs read from Postgre)

# References

- Simple NodeJS Express server (REST API): https://www.codementor.io/olatundegaruba/nodejs-restful-apis-in-10-minutes-q0sgsfhbd
- Consul docker-compose example: https://github.com/alextanhongpin/consul-docker-compose
- Consul BOSH release: https://github.com/cloudfoundry-incubator/consul-release
- Consul Health Checks: https://www.consul.io/docs/agent/checks.html
- Consul Cluster Monitoring: https://www.consul.io/docs/guides/cluster-monitoring-metrics.html
- Consul Telemetry: https://www.consul.io/docs/agent/telemetry.html
- Jeager Client Node: https://github.com/jaegertracing/jaeger-client-node
- Open Tracing Javascript: https://github.com/opentracing/opentracing-javascript
- Distributed Tracing with NodeJS: https://blog.risingstack.com/distributed-tracing-opentracing-node-js/
- Spring Boot Cache with Redis: https://medium.com/@MatthewFTech/spring-boot-cache-with-redis-56026f7da83a (note: the tutorial is missing the @EnableCaching decorator in the Application class)
