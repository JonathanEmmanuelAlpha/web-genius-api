package com.webgenius.webgeniusapi.article;

import com.webgenius.webgeniusapi.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    private String id;

    private String title;

    private Integer readTime;

    private String category;

    private String channel;

    private String thumbnail;

    private Date pubAt;

    private Integer reads;

    private Object content;

    private ObjectId creator;
}
