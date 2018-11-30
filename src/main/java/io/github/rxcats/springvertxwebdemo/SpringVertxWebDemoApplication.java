package io.github.rxcats.springvertxwebdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

@Slf4j
@SpringBootApplication
public class SpringVertxWebDemoApplication {

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    Router router;

    @Autowired
    HttpServer server;

    @EventListener
    public void deploy(ApplicationReadyEvent event) {
        router.getRoutes().forEach(r -> {
            log.info("Mapped: [{}] {}", r.getPath(), r);
        });

        server.requestHandler(router).listen(serverPort, listen -> {
            if (listen.succeeded()) {
                log.info("Server is now listening!" + " (" + serverPort +")");
            } else {
                log.info("Failed to bind");
            }
        });
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringVertxWebDemoApplication.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }
}
