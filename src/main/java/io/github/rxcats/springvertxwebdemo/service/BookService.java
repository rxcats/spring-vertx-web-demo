package io.github.rxcats.springvertxwebdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.rxcats.springvertxwebdemo.domain.entity.Book;
import io.github.rxcats.springvertxwebdemo.repository.BookRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

@Service
public class BookService {

    @Autowired
    Vertx vertx;

    @Autowired
    BookRepository bookRepository;

    public void getBooks(Handler<AsyncResult<List<Book>>> asyncResultHandler) {
        vertx.<List<Book>>executeBlocking(future -> {
            List<Book> all = bookRepository.findAll();
            future.complete(all);
            future.setHandler(asyncResultHandler);
        }, res -> {});
    }

    public void addBook(Book book, Handler<AsyncResult<Book>> asyncResultHandler) {
        vertx.<Book>executeBlocking(future -> {
            Book inserted = bookRepository.insert(book);
            future.complete(inserted);
            future.setHandler(asyncResultHandler);
        }, res -> {});
    }

}
