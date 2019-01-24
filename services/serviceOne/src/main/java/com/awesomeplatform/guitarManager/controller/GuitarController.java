package com.awesomeplatform.guitarManager.controller;

import com.awesomeplatform.guitarManager.model.Guitar;
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

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GuitarController {
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private Tracer jaegerTracer;

    @Cacheable(value = "guitars")
    @RequestMapping("/guitars")
    public Guitar getGuitars(
            @RequestParam(value="brand", defaultValue="Fender") String brand,
            @RequestParam(value="model", defaultValue="Stratocaster") String model,
            @RequestHeader HttpHeaders headers) {
        final Map<String, String> headersHash = headers.toSingleValueMap();

        SpanContext serverSpan = jaegerTracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headersHash));

        Span span = jaegerTracer.buildSpan("Guitar Manager Local Span")
                .asChildOf(serverSpan)
                .start();

        span.log("Custom Message inside Guitar Manager's controller");

        span.finish();

        return new Guitar(counter.incrementAndGet(), brand, model);
    }
}
