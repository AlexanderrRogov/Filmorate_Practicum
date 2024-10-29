import org.home.yandex.practicum.contoller.FilmController;
import org.home.yandex.practicum.contoller.UserController;
import org.home.yandex.practicum.exceptions.ValidationException;
import org.home.yandex.practicum.model.Film;
import org.home.yandex.practicum.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Objects;

public class ModelTest {

    UserController userController = new UserController();
    User testUser = new User(1, "qwerty@gmail.com", "ASD3@#d", "Alex", LocalDate.parse("1978-02-05"));
    User testUserWithoutEmail = new User(1, "", "ASD3@#d", "Alex", LocalDate.parse("1978-02-05"));
    User testUserWithoutName = new User(1, "qwerty@gmail.com", "ASD3@#d", "", LocalDate.parse("1978-02-05"));
    FilmController filmController = new FilmController();
    Film film = new Film(1, "Lord of the Rings", "dsfdfdfsf", LocalDate.parse("2001-02-07"), 180L);
    Film filmWithWrongDuration = new Film(1, "Lord of the Rings", "dsfdfdfsf", LocalDate.parse("2001-02-07"), -1L);
    Film filmWithWrongDate = new Film(1, "Lord of the Rings",
            "dsfdfdfdsdsfdsfsdfsdfdsfdsfsdfsdfsdfsdfsdfsdfdsfsdfsdfsfsdfsdfdsdffsdf", LocalDate.parse("1895-12-27"), 180L);
    Film filmWithWrongDate2 = new Film(1, "Lord of the Rings",
            "dsfdfdfdsdsfdsfsdfsdfdsfdsfsdfsdfsdfsdfsdfsdfdsfsdfsdfsfsdfsdfdsdffsdf", LocalDate.parse("1895-12-28"), 180L);

    @Test
    public void testModelUser() {
        userController.create(testUser);
        var u = userController.users().stream().filter(x -> Objects.equals(x.getId(), testUser.getId())).findFirst();
        assertNotNull(u);
    }

    @Test
    public void testModelUserWithoutEmail() {
        try {
            userController.create(testUserWithoutEmail);
            var u = userController.users().stream().filter(x -> Objects.equals(x.getId(), testUser.getId())).findFirst();
            assertNotNull(u);
        } catch (ValidationException e) {
            assertEquals("Email is required", e.getMessage());
        }
    }

    @Test
    public void testModelUserWithoutName() {
            userController.create(testUserWithoutName);
            var u = userController.users().stream().filter(x -> Objects.equals(x.getName(), testUser.getLogin())).findFirst();
            assertNotNull(u);
        }

    @Test
    public void testModelFilm() {
        filmController.create(film);
        var f = filmController.films().stream().filter(x -> Objects.equals(x.getName(), film.getName())).findFirst();
        assertNotNull(f);
    }

    @Test
    public void testModelFilmWithWrongDuration() {
        try{
        filmController.create(filmWithWrongDuration);
        var f = filmController.films().stream().filter(x -> Objects.equals(x.getName(), film.getName())).findFirst();
        assertNotNull(f);
    } catch (ValidationException e) {
        assertEquals("Film duration is invalid", e.getMessage());
    }
    }

    @Test
    public void testModelFilmWithWrongDate() {
        try{
            filmController.create(filmWithWrongDate);
            var f = filmController.films().stream().filter(x -> Objects.equals(x.getName(), film.getName())).findFirst();
            assertNotNull(f);
        } catch (ValidationException e) {
            assertEquals("Film release date is invalid", e.getMessage());
        }
    }

    @Test
    public void testModelFilmWithWrongDate2() {
            filmController.create(filmWithWrongDate2);
            var f = filmController.films().stream().filter(x -> Objects.equals(x.getName(), film.getName())).findFirst();
            assertNotNull(f);
    }
}

