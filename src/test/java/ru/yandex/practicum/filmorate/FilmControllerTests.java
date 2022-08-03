package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTests {

    private final FilmController filmController;
    protected static Validator validator;

    @BeforeAll
    public static void BeforeAll() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Autowired
    public FilmControllerTests(FilmController filmController) {
        this.filmController = filmController;
    }


    @Test
    public void addFilmTest() {

        Film film = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film.setName("testFilm");
        film.setDescription("testDescr");
        film.setReleaseDate(releaseDate);
        film.setDuration(120);

        filmController.create(film);

        assertEquals(1, filmController.getAll().size());
        assertEquals(filmController.getAll().get(0), film);
    }

    @Test
    public void addFilmWithEmptyName() {
        Film film2 = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film2.setDescription("testDescr");
        film2.setReleaseDate(releaseDate);
        film2.setDuration(120);

        assertEquals(0, filmController.getAll().size());
    }

    @Test
    public void addFilmWithDescrMore200Chars() {
        Film film = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film.setName("testFilm");
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                "ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                "laboris nisi ut aliquip ex ea commodo consequat");
        film.setReleaseDate(releaseDate);
        film.setDuration(120);


        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        assertEquals("maximum length of the description is 200 characters", thrown.getMessage());
    }

    @Test
    public void addFilmWithInvalidReleaseDate() {
        LocalDate invalidReleaseDate = LocalDate.of(1895, 12, 27);
        Film film = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film.setName("testFilm");
        film.setDescription("testDescr");
        film.setReleaseDate(invalidReleaseDate);
        film.setDuration(120);


        Throwable thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        assertEquals("release date should not be earlier than December 28, 1895", thrown.getMessage());
    }

    @Test
    public void addFilmWithNegativeDuration() {
        Film film = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film.setName("testFilm");
        film.setDescription("testDescr");
        film.setReleaseDate(releaseDate);
        film.setDuration(-120);

        assertEquals(0, filmController.getAll().size());

    }

    @Test
    public void addFilmWithoutName() {
        Film film3 = new Film();
        LocalDate releaseDate = LocalDate.of(2020, 01, 01);
        film3.setDescription("testDescr");
        film3.setReleaseDate(releaseDate);
        film3.setDuration(120);

        assertEquals(0, filmController.getAll().size());
    }
}
