package com.webgenius.webgeniusapi.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webgenius.webgeniusapi.dto.ArticleDto;
import com.webgenius.webgeniusapi.user.Role;
import com.webgenius.webgeniusapi.user.User;
import com.webgenius.webgeniusapi.user.UserRepository;
import com.webgenius.webgeniusapi.utils.Response;
import com.webgenius.webgeniusapi.utils.ResponseType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/create")
    public ResponseEntity<String> addArticle(
            @RequestBody ArticleDto data, HttpServletRequest request
    ) throws JsonProcessingException {

        final String token = request.getHeader("X-API-USER");
        User user = userRepository.findByEmail(token);

        if(user == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid user credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.AUTHOR && user.getRole() != Role.ADMIN) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Unauthorized request", ResponseType.ERROR)),
                    HttpStatus.UNAUTHORIZED
            );
        }

        articleService.create(data, user);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Article successfully created", ResponseType.SUCCESS)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{channel}")
    public ResponseEntity<String> updateArticle(
            @RequestBody ArticleDto data, @PathVariable String channel, HttpServletRequest request
    ) throws JsonProcessingException {

        final String token = request.getHeader("X-API-USER");
        final User user = userRepository.findByEmail(token);
        final Article article = articleRepository.findByChannel(channel);

        if(user == null || article == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid user credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.ADMIN && article.getCreator() == user.getId()) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Unauthorized request", ResponseType.ERROR)),
                    HttpStatus.UNAUTHORIZED
            );
        }

        articleService.update(data, channel);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Article successfully updated", ResponseType.SUCCESS)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update-content/{channel}")
    public ResponseEntity<String> updateArticleContent(
            @RequestBody ArticleDto data, @PathVariable String channel, HttpServletRequest request
    ) throws JsonProcessingException {

        final String token = request.getHeader("X-API-USER");
        final User user = userRepository.findByEmail(token);
        final Article article = articleRepository.findByChannel(channel);

        if(user == null || article == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid user credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.ADMIN && article.getCreator() == user.getId()) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Unauthorized request", ResponseType.ERROR)),
                    HttpStatus.UNAUTHORIZED
            );
        }

        articleService.updateContent(data, channel);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Article content successfully updated", ResponseType.SUCCESS)),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete/{channel}")
    public ResponseEntity<String> deleteArticle(
            @PathVariable String channel, HttpServletRequest request
    ) throws JsonProcessingException {

        final String token = request.getHeader("X-API-USER");
        final User user = userRepository.findByEmail(token);
        final Article article = articleRepository.findByChannel(channel);

        if(user == null || article == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid user credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.ADMIN && article.getCreator() == user.getId()) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Unauthorized request", ResponseType.ERROR)),
                    HttpStatus.UNAUTHORIZED
            );
        }

        articleService.remove(article);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Article successfully updated", ResponseType.SUCCESS)),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/get/{channel}")
    public ResponseEntity<String> singleArticle(
            @PathVariable String channel
    ) throws JsonProcessingException {
        final Article article = articleService.findArticle(channel);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(article),
                HttpStatus.OK
        );
    }

    @GetMapping("/author-articles")
    public ResponseEntity<String> authorArticles(
            HttpServletRequest request
    ) throws JsonProcessingException {

        final String token = request.getHeader("X-API-USER");
        final User user = userRepository.findByEmail(token);

        if(user == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid user credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        final List<Article> articles = articleService.findAuthorArticles(user);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(articles),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<String> allArticles(
    ) throws JsonProcessingException {

        final List<Article> articles = articleService.findAllArticles();

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(articles),
                HttpStatus.OK
        );
    }
}
