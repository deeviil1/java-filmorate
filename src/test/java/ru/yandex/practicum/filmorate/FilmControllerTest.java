package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
  private FilmController filmController = new FilmController();

    Film film0 = Film.builder()
            .id(Long.valueOf(23))
            .name("film0")
            .description("some desc0")
            .releaseDate(LocalDate.of(2022, 12, 28))
            .duration(Integer.valueOf(120))
            .build();

    @Test
    public void testFindAllMethodWithEmptyFilmsMap() throws Exception {
        try {
            filmController.getAllFilms();
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Список фильмов пуст");
        }
    }

    @Test
    public void testFindAllMethodWithFilledMap() throws Exception {
        filmController.createFilm(film0);
        assertEquals(filmController.getAllFilms().size(), 1);
    }

    @Test
    public void testCreateMethodWithValidObject() throws Exception {
        Film testFilmObj = filmController.createFilm(film0);
        assertEquals(testFilmObj, film0);
    }


    @Test
    public void testCreateMethodWhenDescNull() throws Exception {
        film0.setDescription(null);
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описание не может быть пустым");
        }
    }

    @Test
    public void testCreateMethodWhenReleaseDateBefore18951228() throws Exception {
        film0.setReleaseDate(LocalDate.of(1894, 12, 28));
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не должна быть раньше: 1895-12-28");
        }
    }

    @Test
    public void testCreateMethodWithNullDuration() throws Exception {
        film0.setDuration(null);
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть указана");
        }
    }

    @Test
    public void testCreateMethodWithNegativeDuration() throws Exception {
        film0.setDuration(Integer.valueOf(-120));
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительным числом");
        }
    }


    @Test
    public void testUpdateMethodWithOtherReleaseDate() throws Exception {
        filmController.createFilm(film0);
        Film film1 = film0;
        film1.setReleaseDate(LocalDate.of(2022, 12, 21));
        try {
            filmController.updateFilm(film1);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть изменена");
        }
    }

    @Test
    public void testUpdateMethodWithValidRequest() throws Exception {
        filmController.createFilm(film0);
        Film film1 = (film0);
        film1.setDescription("other desc");
        filmController.updateFilm(film1);
        assertEquals(film1.getDescription(), "other desc");
    }

    @Test
    public void testUpdateMethodWithWrongId() throws Exception {
        filmController.createFilm(film0);
        Film film1 = (film0);
        film1.setId(Long.valueOf(44));
        try {
            filmController.updateFilm(film1);
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Фильм с ID 44 не найден");
        }
    }
}