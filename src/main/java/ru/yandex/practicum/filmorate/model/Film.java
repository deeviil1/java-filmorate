package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.MinReleaseDate;


import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    private Long id;

    @NotBlank
    @NotEmpty
    private String name;


    @NotNull
    @Size
    private String description;

    @MinReleaseDate
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
