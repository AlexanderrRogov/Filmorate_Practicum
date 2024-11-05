package org.home.yandex.practicum.service;

import org.home.yandex.practicum.dal.DbStorage;
import org.home.yandex.practicum.dal.FilmDbStorage;
import org.home.yandex.practicum.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final DbStorage<Film> filmDbStorage;

    @Autowired
    public FilmService (FilmDbStorage filmDbStorage) {

       this.filmDbStorage = filmDbStorage;
    }

    public Film addLike(int filmId, int userId) {
       return filmDbStorage.addParam(filmId, userId);
    }

    public Film create(Film film) {
       return filmDbStorage.create(film);
    }

    public Film removeLike(int filmId, int userId) {
        var film = filmDbStorage.getCollection().stream().filter(x->x.getId().equals(filmId)).findFirst().orElseThrow();
        filmDbStorage.removeParam(filmId, userId);
        return film;
    }

    public List<Film> getPopular(int count) {
        var films =  filmDbStorage.getCollection();
        return films.stream().sorted(FILM_COMPARATOR).limit(count).collect(Collectors.toList());
    }

    public static final Comparator<Film> FILM_COMPARATOR =
            Comparator.comparingInt(Film::getRate).reversed();

    public List<Film> getFilms() {
       return filmDbStorage.getCollection();
    }

    public Film update(Film film, int id) {
       return filmDbStorage.update(film);
    }

    public Film delete(int id) {
       return filmDbStorage.delete(id);
    }

}

