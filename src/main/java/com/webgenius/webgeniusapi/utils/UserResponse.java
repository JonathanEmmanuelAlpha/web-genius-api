package com.webgenius.webgeniusapi.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webgenius.webgeniusapi.user.Role;
import lombok.Value;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Value
public class UserResponse {

    private ObjectId id;

    private String pseudo;

    private String picture;

    private String bio;

    private String email;

    private boolean hasAuthorDemand;

    private Role role;

    private Date createAt;

    public static UserResponse from(
        final ObjectId id,
        final String pseudo,
        final String picture,
        final String bio,
        final String email,
        final boolean hasAuthorDemand,
        final Role role,
        final Date createAt
    ) {
        return new UserResponse(id, pseudo, picture, bio, email, hasAuthorDemand, role, createAt);
    }
}
