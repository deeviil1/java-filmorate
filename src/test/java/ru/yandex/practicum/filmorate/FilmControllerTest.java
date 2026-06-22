package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmControllerTest {
    private final FilmController filmController = new FilmController();
    private Film film;

    @BeforeEach
    void setUp() {
        film = Film.builder()
                .id(23L)
                .name("film")
                .description("some desc")
                .releaseDate(LocalDate.of(2022, 12, 28))
                .duration(120)
                .build();
    }

    @Test
    public void testFindAllMethodWithEmptyFilmsMap() throws Exception {
        try {
            filmController.getAllFilms();
        } catch (NotFoundException e) {
            assertEquals("Список фильмов пуст", e.getMessage());
        }
    }

    @Test
    public void testFindAllMethodWithFilledMap() throws Exception {
        filmController.createFilm(film);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    public void testCreateMethodWithValidObject() throws Exception {
        Film testFilmObj = filmController.createFilm(film);
        assertEquals(testFilmObj, film);
    }


    @Test
    public void testCreateMethodWhenDescNull() throws Exception {
        film.setDescription(null);
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertEquals("Описание не может быть пустым", e.getMessage());
        }
    }

    @Test
    public void testCreateMethodWithTooLongDescription() throws Exception {
        String tooLongDescription = "Описание фильма, " +
                "которое явно превышает допустимый лимит " +
                "символов для данного поля и составляет более 200 символов, " +
                "например, этот текст можно продолжить для достижения необходимой длины.";
        film.setDescription(tooLongDescription);

        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertEquals("Описание слишком длинное, должно быть не более 200 символов", e.getMessage());
        }
    }

    @Test
    public void testCreateMethodWithNullDuration() throws Exception {
        film.setDuration(null);
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть указана", e.getMessage());
        }
    }

    @Test
    public void testCreateMethodWithNegativeDuration() throws Exception {
        film.setDuration(-120);
        try {
            filmController.createFilm(film);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительным числом", e.getMessage());
        }
    }


    @Test
    public void testUpdateMethodWithOtherReleaseDate() throws Exception {
        filmController.createFilm(film);


        Film filmToUpdate = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(LocalDate.of(2022, 12, 21))
                .duration(film.getDuration())
                .build();

        try {
            filmController.updateFilm(filmToUpdate);
        } catch (ValidationException e) {
            assertEquals("Дата релиза не может быть изменена", e.getMessage());
        }
    }

    @Test
    public void testUpdateMethodWithValidRequest() throws Exception {
        filmController.createFilm(film);
        Film film1 = (film);
        film1.setDescription("other desc");
        filmController.updateFilm(film1);
        assertEquals("other desc", film1.getDescription());
    }

    @Test
    public void testUpdateMethodWithWrongId() throws Exception {
        filmController.createFilm(film);
        Film film1 = (film);
        film1.setId(44L);
        try {
            filmController.updateFilm(film1);
        } catch (NotFoundException e) {
            assertEquals("Фильм с ID 44 не найден", e.getMessage());
        }
    }
}