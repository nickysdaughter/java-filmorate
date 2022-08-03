package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    @Override
    public Film create(Film film) {
        Long id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("movie is added: {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("movie is updated: {}", film);

        return film;
    }

    @Override
    public Film getFilm(Long filmId) {
       return films.get(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private Long generateId() {
        return id++;
    }
}
