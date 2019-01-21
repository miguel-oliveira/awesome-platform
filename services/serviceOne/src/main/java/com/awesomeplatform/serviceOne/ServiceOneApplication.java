package com.awesomeplatform.serviceOne;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableCaching
public class ServiceOneApplication {

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		return new Configuration("Service One", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
				new Configuration.ReporterConfiguration())
				.getTracer();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceOneApplication.class, args);
	}

}

