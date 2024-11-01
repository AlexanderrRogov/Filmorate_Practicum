package org.home.yandex.practicum.dao;

import org.home.yandex.practicum.enums.Genre;
import org.home.yandex.practicum.enums.MotionPictureAssociation;
import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film update(Film film, int id) {
            String sqlQuery = "update FILM set " +
                    "ID = ?, NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPA =? " +
                    "where ID = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getId()
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa());
        return film;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILM (ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, USERLIKE, GENRE, MPA) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa());
        return film;
    }

    @Override
    public Film delete(int id) {
        String sqlQuery = "delete from FILM where id = ?";
        if(!(jdbcTemplate.update(sqlQuery, id) > 0)) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
        return null;
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        String sqlQuery =
                "select ID, " +
                        "NAME, " +
                        "DESCRIPTION, " +
                        "RELEASEDATE, " +
                        "DURATION, " +
                        "USERLIKE, " +
                        "GENRE, " +
                        "MPA " +
                        "from FILM " +
                        "join PUBLIC.FILM_GENRE FG on FILM.ID = FG.FILMID " +
                        "join PUBLIC.FILM_LIKES FL on FILM.ID = FL.FILMID";

        jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return null;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .userLike(Collections.singleton(resultSet.getInt("userLike")))
                .genre(Collections.singleton(resultSet.getObject("genre", Genre.class)))
                .mpa(resultSet.getObject("mpa", MotionPictureAssociation.class))
                .build();
    }
}
