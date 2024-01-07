package com.webgenius.webgeniusapi;


import com.webgenius.webgeniusapi.article.Article;
import com.webgenius.webgeniusapi.dto.EmailDto;
import com.webgenius.webgeniusapi.dto.Login;
import com.webgenius.webgeniusapi.dto.ResetPassword;
import com.webgenius.webgeniusapi.dto.Signup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping("/")
    public String home(Model model) {
        final List<Article> articles = new ArrayList<Article>();

        final Article art1 = new Article(
                "658c27386f500d51c08b0cb0",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "uploads/1.png",
                "18 Avril 2023",
                124
        );
        final Article art2 = new Article(
                "658c27386f500d51c08b0cb1",
                "Class aptent taciti sociosqu ad litora",
                "uploads/2.jpg",
                "24 Mai 2023",
                412
        );
        final Article art3 = new Article(
                "658c27386f500d51c08b0cb1",
                "Suspendisse gravida, tellus ac pretium molestie",
                "uploads/3.jpg",
                "13 Septembre 2023",
                315
        );

        articles.add(art1);
        articles.add(art2);
        articles.add(art3);

        model.addAttribute("articles", articles);

        return "index";
    }
}
