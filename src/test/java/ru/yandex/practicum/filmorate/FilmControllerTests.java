package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTests {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    void init() {
        filmController = new FilmController();
        film = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film.setName("testFilm");
        film.setDescription("testDescr");
        film.setReleaseDate(releaseDate);
        film.setDuration(120);
    }

    @Test
    public void addFilmTest() {
        filmController.createFilm(film);

        assertEquals(1, filmController.listOfFilms().size());
        assertEquals(filmController.listOfFilms().get(0), film);
    }

    @Test
    public void addFilmWithEmptyName() {
        Film film2 = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film2.setName("");
        film2.setDescription("testDescr");
        film2.setReleaseDate(releaseDate);
        film2.setDuration(120);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.createFilm(film2);
        });
        assertEquals("title of the movie cannot be empty", thrown.getMessage());
    }

    @Test
    public void addFilmWithDescrMore200Chars() {
        filmController.createFilm(film);
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                "laboris nisi ut aliquip ex ea commodo consequat");

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });
        assertEquals("maximum length of the description is 200 characters", thrown.getMessage());
    }

    @Test
    public void addFilmWithInvalidReleaseDate() {
        filmController.createFilm(film);
        LocalDate invalidReleaseDate = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(invalidReleaseDate);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });
        assertEquals("release date should not be earlier than December 28, 1895", thrown.getMessage());
    }

    @Test
    public void addFilmWithNegativeDuration() {
        filmController.createFilm(film);
        film.setDuration(-120);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.updateFilm(film);
        });
        assertEquals("duration of the film should be positive", thrown.getMessage());
    }

    @Test
    public void addFilmWithoutName() {
        Film film3 = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film3.setDescription("testDescr");
        film3.setReleaseDate(releaseDate);
        film3.setDuration(120);

        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            filmController.createFilm(film3);
        });
        assertEquals(null, thrown.getMessage());
    }
}
