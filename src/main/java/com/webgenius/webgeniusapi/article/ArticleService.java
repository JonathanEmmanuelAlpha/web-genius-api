package com.webgenius.webgeniusapi.article;

import com.webgenius.webgeniusapi.dto.ArticleDto;
import com.webgenius.webgeniusapi.user.Role;
import com.webgenius.webgeniusapi.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArticleRepository articleRepository;

    public Article findArticle(String channel) {
        return articleRepository.findByChannel(channel);
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> findAuthorArticles(User author) {
        return mongoTemplate.query(Article.class)
                .matching(Query.query(Criteria.where("creator").is(author.getId())))
                .all();
    }

    public void create(ArticleDto data, User user) {
        final Article article = new Article();

        article.setTitle(data.getTitle());
        article.setReadTime(data.getReadTime());
        article.setCategory(data.getCategory());
        article.setThumbnail(data.getThumbnail());
        article.setChannel(data.getChannel());
        article.setCreator(user.getId());
        article.setPubAt(new Date(System.currentTimeMillis()));
        article.setReads(0);

        articleRepository.save(article);
    }

    public void update(ArticleDto data, String channel) {
        Update update = new Update();

        update.set("title", data.getTitle());
        update.set("readTime", data.getReadTime());
        update.set("category", data.getCategory());
        update.set("channel", data.getChannel());
        update.set("pubAt", new Date(System.currentTimeMillis()));

        mongoTemplate.update(User.class)
                .matching(Criteria.where("channel").is(channel))
                .apply(update)
                .all();
    }

    public void updateContent(Object content, String channel) {
        Update update = new Update();

        update.set("content", content);
        update.set("pubAt", new Date(System.currentTimeMillis()));

        mongoTemplate.update(User.class)
                .matching(Criteria.where("channel").is(channel))
                .apply(update)
                .all();
    }

    public void remove(Article article) {

        articleRepository.delete(article);
    }
}
