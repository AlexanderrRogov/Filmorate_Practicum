package org.home.yandex.practicum.storage;

import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.exceptions.ValidationException;
import org.home.yandex.practicum.model.Film;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();

    public Film saveArticle(Film film, int id) {
        if(films.keySet().stream().toList().contains(id)) {
            films.put(id, film);
        } else {
            log.warn("Film with id {} not found", id);
            throw new ValidationException("Film not found");
        }
        return film;
    }


    public Film create(Film film) {
        if(film.getName().toCharArray().length > 200) {
            log.error("Film name too long");
            throw new ValidationException("Film name too long");
        }
        if (film.getName().isEmpty()) {
            log.error("Film name is empty");
            throw new ValidationException("Film name is empty");
        }
        if(film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Film release date is invalid");
            throw new ValidationException("Film release date is invalid");
        }
        if(film.getDuration() <= 0) {
            log.error("Film duration is invalid");
            throw new ValidationException("Film duration is invalid");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film delete(int id) {
        if (id <= 0) {
            log.error("Wrong id number");
            throw new ValidationException("Wrong id number");
        }
        if (films.keySet().stream().toList().contains(id)) {
            log.warn("Can't delete film. Film with id {} not found", id);
            throw new ValidationException("Film not found");
        }
        return films.remove(id);
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return films;
    }
}
