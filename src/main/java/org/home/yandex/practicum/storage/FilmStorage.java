package org.home.yandex.practicum.storage;

import org.home.yandex.practicum.model.Film;

import java.util.HashMap;

public interface FilmStorage {

    Film saveArticle(Film film, int id);
    Film create(Film film);
    Film delete(int id);
    HashMap<Integer, Film> getFilms();
}
