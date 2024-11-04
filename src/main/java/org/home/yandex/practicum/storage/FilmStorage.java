package org.home.yandex.practicum.storage;

import org.home.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {

    Film update(Film film, int id);
    Film create(Film film);
    Film delete(int id);
    List<Film> getFilms();
}
