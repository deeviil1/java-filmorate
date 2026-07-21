package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    Map<Long, Set<Long>> filmsLike = new HashMap<>();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film deleteFilm(Long filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Long filmId) {
       return filmStorage.getFilm(filmId);
    }

    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilms();
    }

    public void addFilmLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        filmsLike.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId);
        userStorage.getUser(userId);

        Set<Long> likedUsers = filmsLike.get(filmId);
        if (likedUsers != null) {
            likedUsers.remove(userId);
        } else {
            throw new ru.yandex.practicum.filmorate.exception.NotFoundException("Лайк от пользователя не найден");
        }
    }

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
