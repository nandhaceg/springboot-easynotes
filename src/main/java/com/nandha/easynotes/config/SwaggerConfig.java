package com.nandha.easynotes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
                                 
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
          .apiInfo(apiInfo())
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.nandha.easynotes.controllers"))
          .paths(regex("/api.*"))
          .build();
    }
    
    private ApiInfo apiInfo() {
    	return new ApiInfoBuilder()
    		.title("NotesApp")
    		.description("Operations on Notes")
    		.version("0.0.1")
    		.build();
    }
}
