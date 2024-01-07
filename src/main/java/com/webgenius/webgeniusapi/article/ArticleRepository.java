package com.webgenius.webgeniusapi.article;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository  extends MongoRepository<Article, ObjectId> {
}
