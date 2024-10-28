package org.home.yandex.practicum.contoller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.exceptions.ValidationException;
import org.home.yandex.practicum.model.Film;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final HashMap<Integer,Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> films() {
        return films.values().stream().toList();
    }

    @PutMapping("/film/{id}")
    public Film saveArticle(@Valid @RequestBody Film film, @PathVariable int id) {
        if(films.keySet().stream().toList().contains(id)) {
            films.put(id, film);
        } else {
            log.warn("Film with id {} not found", id);
            throw new ValidationException("Film not found");
        }
        return film;
    }

    @PostMapping("/film")
    public Film create(@Valid @RequestBody Film film) {
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
}
