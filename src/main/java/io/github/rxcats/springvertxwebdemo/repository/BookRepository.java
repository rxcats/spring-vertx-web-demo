package io.github.rxcats.springvertxwebdemo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.rxcats.springvertxwebdemo.domain.entity.Book;

public interface BookRepository extends MongoRepository<Book, ObjectId> {

}
