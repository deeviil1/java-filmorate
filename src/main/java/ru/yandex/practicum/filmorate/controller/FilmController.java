package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private Long nextId = 1L;


    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Список фильмов получен");
        if (films.isEmpty()){
            throw new NotFoundException("Список фильмов пуст");
        }
        return films.values();

    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма фильма");

        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма");
        if (!films.containsKey(newFilm.getId())) {
            log.error("Ошибка обновления");
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не найден");
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