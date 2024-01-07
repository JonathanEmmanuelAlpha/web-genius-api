package com.webgenius.webgeniusapi.user;

import com.webgenius.webgeniusapi.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Controller
@RequestMapping("/account")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String register(Model model) {
        final Signup user = new Signup();
        model.addAttribute("user", user);
        model.addAttribute("error", null);

        return "account/signup";
    }

    @PostMapping("/signup/consumer")
    public String registerConsumer(
            @Valid @ModelAttribute("user") Signup data,
            BindingResult result,
            Model model
    ) {
        if(data.getPassword().length() < 8) {
            model.addAttribute("error", "Enter a password with more than 07 characters");
            return "account/signup";
        }

        if(!data.getPassword().equals(data.getPasswordConf())) {
            model.addAttribute("error", "The 02 passwords do not match");
            return "account/signup";
        }

        if(userRepository.findByEmail(data.getEmail()) != null) {
            model.addAttribute("error", "Email already in use");
            return "account/signup";
        }

        final User user = userService.createUser(data);

        return "account/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        final Login user = new Login();
        model.addAttribute("user", user);

        return "account/login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        final Login user = new Login();
        model.addAttribute("user", user);

        return "account/login-error";
    }

    @GetMapping("/activation")
    public String activation(Model model) {
        final EmailDto user = new EmailDto();
        model.addAttribute("user", user);

        return "account/activation";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        final EmailDto user = new EmailDto();
        model.addAttribute("user", user);

        return "account/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        final ResetPassword user = new ResetPassword();
        model.addAttribute("user", user);

        return "account/reset-password";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal User user) {

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        model.addAttribute("user", user);
        model.addAttribute("formatedDate", format.format(user.getCreateAt().getTime()));

        final Profile profile = new Profile();
        model.addAttribute("profile", profile);

        return "account/profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(
            @Valid @ModelAttribute("profile") Profile data,
            BindingResult result,
            Model model
    ) {
        System.out.println("BIO: " + data.getBio());
        final User user = userService.updateUser(data);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        model.addAttribute("user", user);
        model.addAttribute("formatedDate", format.format(user.getCreateAt().getTime()));

        return "account/profile";
    }

    @GetMapping("/author-request")
    public String authorRequest(Model model, @AuthenticationPrincipal User user) {

        userService.addRequest(user);

        return "account/reset-password";
    }
}
