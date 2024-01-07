package com.webgenius.webgeniusapi.user;

import com.webgenius.webgeniusapi.dto.Profile;
import com.webgenius.webgeniusapi.dto.Signup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findAllAuthorRequest() {
        return mongoTemplate.query(User.class)
                .matching(Query.query(Criteria.where("authorRequest").is(true)))
                .all();
    }

    public List<User> findAllAuthors() {
        return repository.findByRolesIn(Role.AUTHOR);
    }

    public User createUser(Signup data) {
        final User user = new User();
        final Date date = new Date(System.currentTimeMillis());

        user.setPseudo("Unknown-" + date.getTime());
        user.setBio("Empty");
        user.setPicture("uploads/profiles/default.png");
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setVerified(false);
        user.setCreateAt(date);

        repository.save(user);

        return user;
    }

    public User updateUser(Profile data) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();

        final Date date = new Date(System.currentTimeMillis());

        user.setPseudo(data.getPseudo());
        user.setBio(data.getBio());
        user.setUpdateAt(date);

        Update update = new Update();
        update.set("pseudo", data.getPseudo());
        update.set("bio", data.getBio());
        update.set("updateAt", date);

        mongoTemplate.update(User.class)
            .matching(Criteria.where("email").is(user.getUsername()))
            .apply(update)
            .all()
        ;

        return user;
    }

    public void addRequest(User user) {
        if(!user.getRoles().contains(Role.AUTHOR) && user.isAuthorRequest()) {
            return;
        }

        final Date date = new Date(System.currentTimeMillis());

        mongoTemplate.update(User.class)
                .matching(Criteria.where("email").is(user.getUsername()))
                .apply(new Update().push("authorRequest").value(true))
                .first();
    }
}
