package com.webgenius.webgeniusapi.article;

import com.webgenius.webgeniusapi.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    private String id;

    private String title;

    private String thumbnail;

    private String pubAt;

    private Integer reads;

    private String content;

    @DocumentReference
    private User creator;
}
