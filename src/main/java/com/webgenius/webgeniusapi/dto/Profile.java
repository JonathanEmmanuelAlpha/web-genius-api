package com.webgenius.webgeniusapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @NotEmpty(message = "Pseudo should not be empty")
    private String pseudo;

    @NotEmpty(message = "Bio should not be empty")
    private String bio;
}
