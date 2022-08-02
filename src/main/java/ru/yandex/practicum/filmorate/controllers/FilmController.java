package ru.yandex.practicum.filmorate.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {

        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {

        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll() {

        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {

        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable Long id, @PathVariable Long userId) {

        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable Long id, @PathVariable Long userId) {

        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> mostPopularMovies(@Positive @RequestParam(defaultValue = "10") int count) {

        return filmService.mostPopularMovies(count);
    }
}
