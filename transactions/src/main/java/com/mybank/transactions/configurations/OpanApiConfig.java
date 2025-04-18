package com.mybank.transactions.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpanApiConfig {

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Financial Transactions")
                        .description("These are financial transactions and taxes api")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi hideApis() {
        return GroupedOpenApi.builder().group("default")
                .pathsToMatch("/transactions/v1/**")
                .build();
    }
}

