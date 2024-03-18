package com.perfect.electronic.store.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
@Configuration
public class OpenAPIConfiguration{
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:9090");
        server.setDescription("Electronic Store Development");

        Contact myContact = new Contact();
        myContact.setName("Email");
        myContact.setEmail("your.email@gmail.com");

        License myLicence=new License();
        myLicence.name("Licence OF APIs");
        myLicence.setUrl("http://shaanaircon.in/portfolio/");


        Info information = new Info()
                .title("Electronic Store API")
                .version("1.0")
                .description("This API exposes endpoints to manage Electronic Store.")
                .license(myLicence)
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER).name("Authorization")))
                       .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("bearer-jwt",
//                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER).name("Authorization")))
//
//                .addSecurityItem(
//                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
//    }
}