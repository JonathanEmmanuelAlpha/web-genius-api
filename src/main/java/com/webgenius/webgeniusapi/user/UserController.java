package com.webgenius.webgeniusapi.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webgenius.webgeniusapi.dto.*;
import com.webgenius.webgeniusapi.utils.Response;
import com.webgenius.webgeniusapi.utils.ResponseType;
import com.webgenius.webgeniusapi.utils.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(
            @RequestBody Signup data
    ) throws JsonProcessingException {
        if(userRepository.findByEmail(data.getEmail()) != null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Email already in use", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        final User user = userService.createUser(data);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Account successfully created", ResponseType.SUCCESS)),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/me/{email}")
    public ResponseEntity<String> login(
            @PathVariable String email, HttpServletResponse response
    ) throws JsonProcessingException {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
                );
        }

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(UserResponse.from(
                        user.getId(),
                        user.getPseudo(),
                        user.getPicture(),
                        user.getBio(),
                        user.getEmail(),
                        user.isHasAuthorDemand(),
                        user.getRole(),
                        user.getCreateAt()
                )),
                HttpStatus.OK
        );
    }

    @PutMapping("/author-request/{email}")
    public ResponseEntity<String> authorRequest(
            @PathVariable String email
    ) throws JsonProcessingException {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        userService.addRequest(user);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Request successfully sent", ResponseType.SUCCESS)),
                HttpStatus.OK
        );
    }

    @PutMapping("/author-accept/{email}")
    public ResponseEntity<String> acceptAuthor(
            @PathVariable String email, HttpServletRequest request
    ) throws JsonProcessingException {
        final String token = request.getHeader("X-API-USER");

        User user = userRepository.findByEmail(token);
        User target = userRepository.findByEmail(email);

        if(user == null || target == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.ADMIN || target.getRole() == Role.AUTHOR) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Author request not allowed", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        userService.addAuthor(target);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Author added successfully", ResponseType.SUCCESS)),
                HttpStatus.OK
        );
    }

    @PutMapping("/author-reject/{email}")
    public ResponseEntity<String> rejectAuthor(
            @PathVariable String email, HttpServletRequest request
    ) throws JsonProcessingException {
        final String token = request.getHeader("X-API-USER");

        User user = userRepository.findByEmail(token);
        User target = userRepository.findByEmail(email);

        if(user == null || target == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid credentials", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(user.getRole() != Role.ADMIN || target.getRole() == Role.AUTHOR) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Author request not allowed", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        userService.rejectAuthor(target);

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Author demand rejected", ResponseType.SUCCESS)),
                HttpStatus.OK
        );
    }

    @GetMapping("/authors/requests")
    public ResponseEntity<String> allAuthorsRequests() throws JsonProcessingException {
        List<User> authors = userService.findAllAuthorRequest();

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(authors),
                HttpStatus.OK
        );
    }

    @GetMapping("/authors/all")
    public ResponseEntity<String> allAuthors() throws JsonProcessingException {
        List<User> authors = userService.findAllAuthors();

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(authors),
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody Login data, HttpServletResponse response
    ) throws JsonProcessingException {

        User user = userService.authenticate(data);

        if(user == null) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(Response.from("Invalid email or password", ResponseType.ERROR)),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from(user.getEmail(), ResponseType.SUCCESS)),
                HttpStatus.OK
        );
    }

    @PostMapping("/login-error")
    public ResponseEntity<String> loginError(
            @RequestBody Login data
    ) throws JsonProcessingException {

        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(Response.from("Invalid user email or password", ResponseType.ERROR)),
                HttpStatus.BAD_REQUEST
        );
    }
}
