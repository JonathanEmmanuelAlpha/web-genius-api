package com.webgenius.webgeniusapi.dashboard;

import com.webgenius.webgeniusapi.article.Article;
import com.webgenius.webgeniusapi.article.ArticleRepository;
import com.webgenius.webgeniusapi.user.User;
import com.webgenius.webgeniusapi.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String home(Model model, @AuthenticationPrincipal User user) {

        final List<User> targets = userService.findAllAuthorRequest();

        model.addAttribute("user", user);
        model.addAttribute("targets", targets);

        return "dashboard/index";
    }

    @GetMapping("/authors")
    public String authors(Model model, @AuthenticationPrincipal User user) {

        final List<User> authors = userService.findAllAuthors();

        model.addAttribute("user", user);
        model.addAttribute("authors", authors);

        return "dashboard/authors";
    }

    @GetMapping("/articles")
    public String articles(Model model, @AuthenticationPrincipal User user) {

        final List<Article> articles = repository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("articles", articles);

        return "dashboard/articles";
    }

    @GetMapping("new-article")
    public String articleConsumer(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return "dashboard/new-article";
    }

    @PostMapping("new-article/consumer")
    public String newArticle(Model model, @AuthenticationPrincipal User user, @Valid @ModelAttribute("article") Article data) {
        model.addAttribute("user", user);

        return "dashboard/new-article/consumer";
    }
}
