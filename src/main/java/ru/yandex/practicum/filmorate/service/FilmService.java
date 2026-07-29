package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmServiceInterface {

    private final Map<Long, Set<Long>> filmsLike = new HashMap<>();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film deleteFilm(Long filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }

    @Override
    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilms();
    }

    @Override
    public void addFilmLike(Long filmId, Long userId) {

        if (filmStorage.getFilm(filmId) == null) {
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }

        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        filmsLike.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

        if (filmStorage.getFilm(filmId) == null) {
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }

        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        Set<Long> likedUsers = filmsLike.get(filmId);

        if (likedUsers == null || !likedUsers.contains(userId)) {
            throw new NotFoundException("Лайк от пользователя с ID " + userId + " для фильма " + filmId + " не найден");
        }

        likedUsers.remove(userId);
    }

    @Override
    public Collection<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> {
                    int likes1 = filmsLike.getOrDefault(f1.getId(), Collections.emptySet()).size();
                    int likes2 = filmsLike.getOrDefault(f2.getId(), Collections.emptySet()).size();
                    return Integer.compare(likes2, likes1);
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
