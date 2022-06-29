package ru.yandex.practicum.filmorate.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;
    private static final LocalDate RELEASE_DATE_VALID = LocalDate.of(1895, 12, 28);

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        validation(film);
        Long id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("movie is added: {}", film);

        return film;
    }

    @SneakyThrows
    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId()< 0) {
            throw new ValidationException("invalid id");
        }
        validation(film);
        films.put(film.getId(), film);
        log.info("movie is updated: {}", film);

        return film;
    }

    @GetMapping(value = "/films")
    public List<Film> listOfFilms() {

        return new ArrayList<>(films.values());
    }

    private Long generateId() {
        return id++;
    }

    @SneakyThrows
    private static void validation(Film film) {
        try {
            if (film.getName().equals("")) {
                log.debug("movie title is empty");
                throw new ValidationException("title of the movie cannot be empty");
            }
            if (film.getDescription().length() > 200) {
                log.debug("length of the description is more than 200 characters");
                throw new ValidationException("maximum length of the description is 200 characters");
            }
            if (film.getReleaseDate().isBefore(RELEASE_DATE_VALID)) {
                log.debug("release date should is earlier than December 28, 1895: {}", film.getReleaseDate());
                throw new ValidationException("release date should not be earlier than December 28, 1895");
            }
            if (film.getDuration() < 0) {
                log.debug("duration of the film is negative: {}", film.getDuration());
                throw new ValidationException("duration of the film should be positive");
            }
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}
