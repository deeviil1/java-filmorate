package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film addFilm(Film film) {
        log.info("Добавление фильма фильма");

        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Long filmId){
        Film film = getFilm(filmId);
        return films.remove(filmId);
    }

    @Override
    public Film updateFilm(Film newFilm){

        log.info("Обновление фильма");

        if (!films.containsKey(newFilm.getId())){
            log.error("Ошибка обновления");
            throw new NotFoundException("Фильм с ID " + newFilm.getId() + " не найден");
        }
        films.put(newFilm.getId(), newFilm);

        log.info("Фильм обновлен");

        return newFilm;
    }

    @Override
    public Film getFilm(Long filmId){
        Film film = films.get(filmId);
        if (film == null){
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms(){

        log.info("Список фильмов получен");

        return new ArrayList<>(films.values());
    }
}
