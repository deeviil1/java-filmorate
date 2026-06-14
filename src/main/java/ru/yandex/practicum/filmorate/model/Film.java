package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;



import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {

   private Long id;
    @NotBlank
    private String name;

    @Size
   private String description;

    @JsonFormat(pattern = "dd.MM.yyyy")
   private LocalDate releaseDate;

@Positive
    private Integer duration;
}
