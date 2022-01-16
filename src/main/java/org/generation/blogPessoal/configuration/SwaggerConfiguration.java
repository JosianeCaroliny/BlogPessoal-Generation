package org.generation.blogPessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI springBlogPessoalOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Projeto Blog Pessoal")
					.description("Projeto Blog Pessoal - Generation Brasil")
					.version("v0.0.1")
				.license(new License()
					.name("Generation Brasil")
					.url("<http://brazil.generation.org>"))
				.contact(new Contact()
					.name("Josiane Caroliny")
					.url("<https://github.com/JosianeCaroliny>")
					.email("carolinyjosiane@gmail.com")))
				.externalDocs(new ExternalDocumentation()
					.description("Github Project")
					.url("https://github.com/JosianeCaroliny/BlogPessoal-Generation"));
	}
	
	private ApiResponse createApiResponse(String message) {
		return new ApiResponse().description(message);
	}

	@Bean
	public OpenApiCustomiser customerGlobalResponseStatus() {
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
				ApiResponses apiResponses = operation.getResponses();
				
				apiResponses.addApiResponse("200", createApiResponse("Sucess!"));
				apiResponses.addApiResponse("201", createApiResponse("Created!"));
				apiResponses.addApiResponse("400", createApiResponse("Request error!"));
				apiResponses.addApiResponse("401", createApiResponse("Not authorized!"));
				apiResponses.addApiResponse("500", createApiResponse("Internal server Error!"));

			}));
		};
	}

}