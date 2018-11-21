package io.github.rxcats.springvertxwebdemo.vertx;

import lombok.extern.slf4j.Slf4j;

import io.github.rxcats.springvertxwebdemo.domain.message.ResponseEntity;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

@Slf4j
public class VertxResponseHandler {

    public static void errorResponse(RoutingContext ctx, String details) {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        ctx.response().end(Json.encode(ResponseEntity.error(details)));
    }

    public static <T> void response(RoutingContext ctx, AsyncResult<T> ar) {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        if (ar.succeeded()) {
            ctx.response().end(Json.encode(ResponseEntity.of(ar.result())));
        } else {
            ctx.response().end(Json.encode(ResponseEntity.error(ar.cause().getMessage())));
        }
    }

}
