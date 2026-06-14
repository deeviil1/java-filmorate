package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {

    Long id;

    @Email
    @NonNull
    String email;

    @NonNull
    @NotBlank
    String login;

    String name;


    @PastOrPresent
    LocalDate birthday;

}
