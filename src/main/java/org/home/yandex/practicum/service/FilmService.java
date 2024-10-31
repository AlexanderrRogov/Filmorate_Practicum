package org.home.yandex.practicum.service;

import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.storage.FilmStorage;
import org.home.yandex.practicum.storage.InMemoryFilmStorage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService (InMemoryFilmStorage inMemoryFilmStorage) {
        filmStorage = inMemoryFilmStorage;
    }

    public Film addLike(int filmId, int userId) {
        var film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        film.getUserLike().add(userId);
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        var film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        film.getUserLike().remove(userId);
        return film;
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

    public static final Comparator<Film> FILM_COMPARATOR =
            Comparator.comparingInt(Film::getRate).reversed();
}

