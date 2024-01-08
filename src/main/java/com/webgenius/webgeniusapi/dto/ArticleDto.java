package com.webgenius.webgeniusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private String title;

    private Integer readTime;

    private String category;

    private String channel;

    private String thumbnail;
}
