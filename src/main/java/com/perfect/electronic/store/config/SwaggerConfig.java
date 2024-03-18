//package com.perfect.electronic.store.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.ArrayDeque;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public Docket docket(){
//        Docket docket=new Docket(DocumentationType.SWAGGER_2);
//        docket.apiInfo(getApiInfo())
//
//        return  docket;
//    }
//
//    private ApiInfo getApiInfo() {
//        ApiInfo apiInfo=new ApiInfo(
//                "Electronic Store Backend:APIS",
//                "This is backed created By Us",
//                "1.0.0v",
//                "http://localhost:9090/",
//                new Contact("Shakir","http://localhost:9090/","abc@gmail.com"),
//                "Licence of APIS",
//                "http://localhost:9090/",
//                new ArrayDeque<>()
//        );
//        return  apiInfo;
////        return new Docket(DocumentationType.SWAGGER_2)
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any())
////                .build();
//    }
//
//}
