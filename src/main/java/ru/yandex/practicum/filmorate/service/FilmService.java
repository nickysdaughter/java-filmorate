package ru.yandex.practicum.filmorate.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate RELEASE_DATE_VALID = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        checkFilmValidity(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        checkFilmExists(film.getId());
        checkFilmValidity(film);
        return filmStorage.update(film);
    }

    public Film getFilm(Long id) {
        checkFilmExists(id);
        return filmStorage.getFilm(id);
    }

    public void addLike(Long filmId,Long userId) {
        checkFilmExists(filmId);
        userService.checkUserExists(userId);
        //todo проверить юзера!
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLike(Long filmId,Long userId) {
        checkFilmExists(filmId);
        userService.checkUserExists(userId);
        filmStorage.getFilm(filmId).removeLike(userId);
    }

    public List<Film> mostPopularMovies(int count) {
        return filmStorage.getAll().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private void checkFilmValidity(Film film) {
        try {

            if (film.getDescription().length() > 200) {
                log.debug("length of the description is more than 200 characters");
                throw new ValidationException("maximum length of the description is 200 characters");
            }
            if (film.getReleaseDate().isBefore(RELEASE_DATE_VALID)) {
                log.debug("release date should is earlier than December 28, 1895: {}", film.getReleaseDate());
                throw new ValidationException("release date should not be earlier than December 28, 1895");
            }
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    @SneakyThrows
    private void checkFilmExists(Long id) {
        boolean isFilmNotExist = filmStorage.getAll().stream()
                .noneMatch(film -> film.getId().equals(id));
        if (isFilmNotExist) {
            throw new NoSuchElementException("There is no movie with such an id");
        }

    }
}
