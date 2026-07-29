package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmServiceInterface {

    Film addFilm(Film film);

    Film deleteFilm(Long filmId);

    Film updateFilm(Film film);

    Film getFilm(Long filmId);

    Collection<Film> getAllFilm();

    void addFilmLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Collection<Film> getTopFilms(int count);
}


