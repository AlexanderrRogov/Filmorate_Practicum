package org.home.yandex.practicum.dao;

import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.User;
import org.home.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User update(User user, int id) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User delete(int id) {
        return null;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        String sqlQuery =
                "select ID, " +
                        "EMAIL, " +
                        "LOGIN, " +
                        "NAME, " +
                        "BIRTHDAY " +
                        "from USER_FILMORATE join SUBSCRIBER_STATUS ";

        jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        return null;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Set<SubscriberStatus>> valueMap = new HashMap<>();

        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friendsIds((Set<SubscriberStatus>) resultSet.getObject("friendsIds"))
                .build();
    }
}
