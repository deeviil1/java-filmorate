package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.MinReleaseDate;


import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    @NonNull
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @MinReleaseDate(message = "Дата релиза должна быть не раньше 28.12.1895")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;
}
