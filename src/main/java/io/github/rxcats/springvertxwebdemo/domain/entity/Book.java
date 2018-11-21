package io.github.rxcats.springvertxwebdemo.domain.entity;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Book {

    @Id
    ObjectId id;

    String isbn;

    String name;

    String author;

    Instant createdAt;

}
