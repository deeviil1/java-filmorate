package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private Long nextId = 1L;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Список фильмов получен");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма фильма");

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Ошибка даты релиза");
            throw new ValidationException("Дата релиза не должна быть раньше: " + MIN_RELEASE_DATE);
        }


        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма");

        if (newFilm.getId() == null) {
            log.error("Ошибка обновления");
            throw new ValidationException("id фильма должен быть указан");

        }
        if (!films.containsKey(newFilm.getId())) {
            log.error("Ошибка обновления");
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не найден");
        }

        if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Ошибка даты релиза");
            throw new ValidationException("Дата релиза не должна быть раньше: " + MIN_RELEASE_DATE);
        }

        if (newFilm.getDuration() != null && newFilm.getDuration() < 0) {
            log.error("Длительность фильма не может быть отрицательной");
            throw new ValidationException("Длительность фильма должна быть положительным числом");
        }

        Film oldFilm = films.get(newFilm.getId());

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());

        log.info("Фильм обновлен");

        return oldFilm;
    }
}





