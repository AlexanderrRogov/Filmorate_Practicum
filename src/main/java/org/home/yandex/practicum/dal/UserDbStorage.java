package org.home.yandex.practicum.dal;

import lombok.SneakyThrows;
import org.home.yandex.practicum.Utils.Util;
import org.home.yandex.practicum.enums.FriendshipStatus;
import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.exceptions.ServerSideException;
import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@Component
public class UserDbStorage implements DbStorage<User> {

    private final JdbcTemplate jdbcTemplate;
    private final Connection conn;

    @SneakyThrows
    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        conn = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource()));
        conn.setAutoCommit(false);
    }

    @SneakyThrows
    @Override
    public User update(User user) {
        try {
            String sqlQuery = "update USER_FILMORATE set ID = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ?, EMAIL = ? " +
                    "where ID = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getId(),
                    user.getName(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getEmail(), user.getId());
            conn.commit();
            String sqlQueryInsert = "update SUBSCRIBER_STATUS into set USERID = ?, SUBSCRIBERID = ?, FRIENDSHIPSTATUS = ? " +
                    "where USERID = ?";
            Util.batchUpdateValues(conn, jdbcTemplate, user.getFriendsIds(), sqlQueryInsert);
        } catch (SQLException e) {
            conn.rollback();
            throw new ServerSideException(e.getMessage());
        }
        return get(user.getId());
    }

    @SneakyThrows
    @Override
    public User create(User user) {
        String sqlQuery = "insert into USER_FILMORATE (ID , NAME, LOGIN, BIRTHDAY, EMAIL) " +
                "values (%s, %s, %s, %s, %s)";
        String sql = String.format(sqlQuery, user.getId(), user.getName(), user.getLogin(), user.getBirthday(), user.getEmail());
        int userId = getAutogeneratedId(sql);
        return get(userId);
    }

    @SneakyThrows
    public int getAutogeneratedId(String query) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "id");
            return ps;
        }, keyHolder);
        conn.commit();
        return (int) keyHolder.getKey();
    }

    @SneakyThrows
    @Override
    public User delete(int id) {
        try {
            User user = get(id);
            String sqlQuery = "delete from USER_FILMORATE where ID = ?";
            jdbcTemplate.update(sqlQuery, id);
            conn.commit();
            return user;
        } catch (SQLException e) {
            conn.rollback();
            throw new ServerSideException(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public User addParam(int userId, int subId) {
        try {
            String sqlQuery = "insert into SUBSCRIBER_STATUS (USERID, SUBSCRIBERID, FRIENDSHIPSTATUS) values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userId, subId, FriendshipStatus.FRIEND);
            conn.commit();
            return get(userId);
        } catch (SQLException e) {
            conn.rollback();
            throw new ServerSideException(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public User removeParam(int userId, int subId) {
        try {
            String sqlQuery = "delete from SUBSCRIBER_STATUS where SUBSCRIBERID = ?";
            jdbcTemplate.update(sqlQuery, subId);
            conn.commit();
            return get(userId);
        } catch (SQLException e) {
            conn.rollback();
            throw new ServerSideException(e.getMessage());
        }
    }

    @Override
    public User get(int id) {
        String sqlQuery =
                "select ID, " +
                        "EMAIL, " +
                        "LOGIN, " +
                        "NAME, " +
                        "BIRTHDAY " +
                        "from USER_FILMORATE where ID = ?";
        var user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        String sqlQuerySubStatus =
                "select ID, " +
                        "USERID, " +
                        "SUBSCRIBERID, " +
                        "FRIENDSHIPSTATUS " +
                        "from SUBSCRIBER_STATUS where USERID = ?";

        var subStatus = jdbcTemplate.query(sqlQuerySubStatus, this::mapRowToSubStatus, id);

            for (SubscriberStatus subscriberStatus : subStatus) {
                if (user.getId().equals(subscriberStatus.getUserId())) {
                    user.addNewFriend(subscriberStatus);
                }
            }

        return user;
    }

    @Override
    public List<User> getCollection() {
        String sqlQuery =
                "select ID, " +
                        "EMAIL, " +
                        "LOGIN, " +
                        "NAME, " +
                        "BIRTHDAY " +
                        "from USER_FILMORATE";
        var users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);

        String sqlQuerySubStatus =
                "select ID, " +
                        "USERID, " +
                        "SUBSCRIBERID, " +
                        "FRIENDSHIPSTATUS " +
                        "from SUBSCRIBER_STATUS";

        var subStatus = jdbcTemplate.query(sqlQuerySubStatus, this::mapRowToSubStatus);

        for (User user : users) {
            for (SubscriberStatus subscriberStatus : subStatus) {
                if (user.getId().equals(subscriberStatus.getUserId())) {
                    user.addNewFriend(subscriberStatus);
                }
            }
        }
      return users;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private SubscriberStatus mapRowToSubStatus(ResultSet resultSet, int rowNum) throws SQLException {

        return SubscriberStatus.builder()
                .id(resultSet.getInt("id"))
                .userId(resultSet.getInt("userId"))
                .subscriberId(resultSet.getInt("subscriberId"))
                .friendshipStatus(FriendshipStatus.valueOf(resultSet.getString("friendshipStatus")))
                .build();
    }
}
