package org.home.yandex.practicum;

import org.home.yandex.practicum.dal.FilmDbStorage;
import org.home.yandex.practicum.dal.UserDbStorage;
import org.home.yandex.practicum.enums.Genre;
import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.model.UserLike;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.HashSet;

@JdbcTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class SqlTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        HashSet<UserLike> likes = new HashSet<>();
        likes.add(new UserLike(1,2,3));
        likes.add(new UserLike(1,2,3));
        likes.add(new UserLike(1,2,3));
        HashSet<org.home.yandex.practicum.model.Genre> genres = new HashSet<>();
        genres.add(new org.home.yandex.practicum.model.Genre(1, 2, Genre.DOCUMENTARY));
        genres.add(new org.home.yandex.practicum.model.Genre(1, 2, Genre.ACTION));
        genres.add(new org.home.yandex.practicum.model.Genre(1, 2, Genre.DRAMA));
        Film film = Film.builder().id(3).name("sdasd")
                .mpa("PG").description("2323").duration(120L)
                .dateProd(LocalDate.ofEpochDay(2000-01-01)).genre(genres).userLike(likes).build();
        filmDbStorage.create(film);
    }

    @Test
    public void test1() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.getCollection();
    }

    @Test
    public void test2() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.getCollection();
    }
}
