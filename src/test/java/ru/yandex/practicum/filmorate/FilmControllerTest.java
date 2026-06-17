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
  private Film film0;

      @BeforeEach
    void setUp() {
          film0 = Film.builder()
                  .id(23L)
                  .name("film0")
                  .description("some desc0")
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
        filmController.createFilm(film0);
        assertEquals(1, filmController.getAllFilms().size());
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
            assertEquals("Описание не может быть пустым", e.getMessage());
        }
    }

    @Test
    public void testCreateMethodWithNullDuration() throws Exception {
        film0.setDuration(null);
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть указана", e.getMessage());
        }
    }

    @Test
    public void testCreateMethodWithNegativeDuration() throws Exception {
        film0.setDuration(-120);
        try {
            filmController.createFilm(film0);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительным числом", e.getMessage());
        }
    }


    @Test
    public void testUpdateMethodWithOtherReleaseDate() throws Exception {
        filmController.createFilm(film0);


        Film filmToUpdate = Film.builder()
                .id(film0.getId())
                .name(film0.getName())
                .description(film0.getDescription())
                .releaseDate(LocalDate.of(2022, 12, 21))
                .duration(film0.getDuration())
                .build();

        try {
            filmController.updateFilm(filmToUpdate);
        } catch (ValidationException e) {
            assertEquals("Дата релиза не может быть изменена", e.getMessage());
        }
    }

    @Test
    public void testUpdateMethodWithValidRequest() throws Exception {
        filmController.createFilm(film0);
        Film film1 = (film0);
        film1.setDescription("other desc");
        filmController.updateFilm(film1);
        assertEquals("other desc", film1.getDescription());
    }

    @Test
    public void testUpdateMethodWithWrongId() throws Exception {
        filmController.createFilm(film0);
        Film film1 = (film0);
        film1.setId(44L);
        try {
            filmController.updateFilm(film1);
        } catch (NotFoundException e) {
            assertEquals("Фильм с ID 44 не найден", e.getMessage());
        }
    }
}