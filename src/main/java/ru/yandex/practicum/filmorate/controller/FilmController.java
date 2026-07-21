package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;


@RestController
@RequestMapping("/films")
public class FilmController{
    private final FilmService filmService;

    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms(){
        return filmService.getAllFilm();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film){
        return filmService.addFilm(film);
    }

    @PutMapping("/{id}")
    public Film updateFilm(@Valid @RequestBody Film newFilm){
        return filmService.updateFilm(newFilm);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id){
      return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addFilmLike(@PathVariable Long id, @PathVariable Long userId){
        filmService.addFilmLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable Long id, @PathVariable Long userId){
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count){
        return filmService.getTopFilms(count);
    }

}