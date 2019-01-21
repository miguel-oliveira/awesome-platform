package com.awesomeplatform.serviceOne.controller;

import com.awesomeplatform.serviceOne.model.Greeting;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private Tracer jaegerTracer;

    @Cacheable(value = "greeting-get")
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name, @RequestHeader HttpHeaders headers) {
        final Map<String, String> headersHash = headers.toSingleValueMap();

        SpanContext serverSpan = jaegerTracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headersHash));

        Span span = jaegerTracer.buildSpan("localSpan")
                .asChildOf(serverSpan)
                .start();

        span.log("Custom Message inside Service One's controller");

        span.finish();

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}
