package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage{

    Film addFilm(Film film);
    Film deleteFilm(Long filmId);
    Film updateFilm(Film film);
    Film getFilm(Long filmId);
    Collection<Film> getAllFilms();
}
