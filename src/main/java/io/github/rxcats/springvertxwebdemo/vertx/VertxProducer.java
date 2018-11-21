package io.github.rxcats.springvertxwebdemo.vertx;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.springvertxwebdemo.exception.ServiceException;
import io.github.rxcats.springvertxwebdemo.serializer.LocalDateTimeSerializer;
import io.github.rxcats.springvertxwebdemo.serializer.LocaleSerializer;
import io.github.rxcats.springvertxwebdemo.serializer.ObjectIdDeSerializer;
import io.github.rxcats.springvertxwebdemo.serializer.ObjectIdSerializer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

@Slf4j
@Component
public class VertxProducer {

    @Value("${server.port}")
    private int serverPort;

    private Vertx vertx;
    private Router router;
    private HttpServer server;

    private void customizeJsonMapper() {
        var simpleModule = new SimpleModule();
        simpleModule.addSerializer(Locale.class, new LocaleSerializer());
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        simpleModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
        simpleModule.addDeserializer(ObjectId.class, new ObjectIdDeSerializer());
        Json.mapper.registerModules(new JavaTimeModule(), simpleModule);
        Json.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private void handleCors() {
        // @formatter:off
        router.route().handler(
            CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.HEAD)
                .allowedHeader("*")
        );
        // @formatter:on
    }

    private void handleError() {
        router.route().failureHandler(ctx -> {
            log.error("service exception", ctx.failure());
            if (ctx.failed() && ctx.failure() instanceof ServiceException) {
                VertxResponseHandler.errorResponse(ctx, ((ServiceException) ctx.failure()).getDetails());
            }
        });
    }

    @PostConstruct
    void init() {

        vertx = Vertx.vertx();

        router = Router.router(vertx);

        handleCors();

        handleError();

        router.route().handler(BodyHandler.create());

        var options = new HttpServerOptions().setLogActivity(true).setTcpKeepAlive(true);

        server = vertx.createHttpServer(options);

        server.requestHandler(router).listen(serverPort, listen -> {
            if (listen.succeeded()) {
                log.info("Server is now listening!");
            } else {
                log.info("Failed to bind");
            }
        });

        customizeJsonMapper();
    }

    @Bean(destroyMethod = "")
    Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean(destroyMethod = "")
    Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Bean(destroyMethod = "")
    HttpServer httpServer() {
        return server;
    }

    @PreDestroy
    void close() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> future = new CompletableFuture<>();
        server.close(ar -> future.complete(null));
        vertx.close(ar -> future.complete(null));
        future.get();

        log.info("Shutdown completed.");
    }

}
