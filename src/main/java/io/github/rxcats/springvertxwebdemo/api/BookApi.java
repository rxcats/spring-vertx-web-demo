package io.github.rxcats.springvertxwebdemo.api;

import static io.github.rxcats.springvertxwebdemo.vertx.VertxResponseHandler.response;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.rxcats.springvertxwebdemo.domain.entity.Book;
import io.github.rxcats.springvertxwebdemo.domain.message.ResultCode;
import io.github.rxcats.springvertxwebdemo.exception.ServiceException;
import io.github.rxcats.springvertxwebdemo.service.BookService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@Component
public class BookApi {

    @Autowired
    Router router;

    @Autowired
    BookService bookService;

    @PostConstruct
    void init() {
        router.get("/error").handler(this::errorTest);
        router.post("/books").handler(this::addBook);
        router.get("/books").handler(this::getBooks);
    }

    private void errorTest(RoutingContext ctx) {
        ctx.fail(new ServiceException(ResultCode.error, "just error test"));
    }

    private void addBook(RoutingContext ctx) {
        Book book = ctx.getBodyAsJson().mapTo(Book.class);
        bookService.addBook(book, ar -> response(ctx, ar));
    }

    private void getBooks(RoutingContext ctx) {
        bookService.getBooks(ar -> response(ctx, ar));
    }

}
