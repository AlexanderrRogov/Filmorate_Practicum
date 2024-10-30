package org.home.yandex.practicum.service;

import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.storage.FilmStorage;
import org.home.yandex.practicum.storage.InMemoryFilmStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService (InMemoryFilmStorage inMemoryFilmStorage) {
        filmStorage = inMemoryFilmStorage;
    }

    public Film addLike(int filmId, int userId) {
        var film = filmStorage.getFilms().get(filmId);
        var newLikeSet = film.getUserLike();
        newLikeSet.add(userId);
        film.setUserLike(newLikeSet);
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        var film = filmStorage.getFilms().get(filmId);
        var newLikeSet = film.getUserLike();
        newLikeSet.remove(userId);
        film.setUserLike(newLikeSet);
        return film;
    }

    public List<Film> getTenMostPopularFilms(int count) {
        List<Film> moreLikedFilms = new ArrayList<>();
        List<Film> copy = new ArrayList<>(filmStorage.getFilms().values());
        for (int i = 0; i < count; i++) {
            var film = copy.stream().max(Comparator.comparing(x->x.getUserLike().size())).orElseThrow();
            moreLikedFilms.add(film);
            copy.remove(film);
        }
        return moreLikedFilms;
    }
}

