package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties
public class StartServer {

    private static final String SWAGGER_UI = "http://localhost:8080/tds/swagger-ui.html";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(StartServer.class, args);
        openHomePage();
    }

    private static void openHomePage() throws IOException {
        Runtime rt = Runtime.getRuntime();
        rt.exec("rundll32 url.dll,FileProtocolHandler" + SWAGGER_UI);
    }
}
