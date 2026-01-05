package com.zifty.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.zifty.lambda.LambdaHandler.*;

@SpringBootApplication
public class LambdaApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LambdaApplication.class, args);
		FunctionCatalog functionCatalog = context.getBean(FunctionCatalog.class);

		Function<Mono<Request>, Mono<Map<String, Object>>> function = functionCatalog.lookup("handleRequest");
		function.apply(Mono.fromSupplier(() -> new Request("lokesh", "todo")))
				.block();
	}

}
