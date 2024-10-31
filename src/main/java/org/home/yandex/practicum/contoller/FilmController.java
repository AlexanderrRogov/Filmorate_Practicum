package org.home.yandex.practicum.contoller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.service.FilmService;
import org.home.yandex.practicum.storage.FilmStorage;
import org.home.yandex.practicum.storage.InMemoryFilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        filmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> films() {
        return filmStorage.getFilms().values().stream().toList();
    }

    @PutMapping("/film/{id}")
    public Film saveArticle(@Valid @RequestBody Film film, @PathVariable int id) {
      return filmStorage.saveArticle(film, id);
    }

    @PostMapping("/film")
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @DeleteMapping("/film/{id}")
    public Film create(@PathVariable int id) {
        return filmStorage.delete(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film saveArticle(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> showMostPopularFilms(@RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}
