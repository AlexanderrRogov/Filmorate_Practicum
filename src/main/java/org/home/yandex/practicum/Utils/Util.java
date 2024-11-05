package org.home.yandex.practicum.Utils;

import lombok.SneakyThrows;
import org.home.yandex.practicum.model.Genre;
import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.UserLike;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class Util {
    @SneakyThrows
    public static  <T> void batchUpdateValues(Connection conn, JdbcTemplate jdbcTemplate, Set<T> values, String sql) {
        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        T value = values.stream().toList().get(i);
                        if (value instanceof Genre) {
                            ps.setInt(1, ((Genre) value).getFilmId());
                            ps.setString(2, String.valueOf(((Genre) value).getFilmGenre()));
                            ps.setInt(3, ((Genre) value).getFilmId());
                        }
                        if (value instanceof UserLike) {
                            ps.setInt(1, ((UserLike) value).getFilmId());
                            ps.setInt(2, ((UserLike) value).getUserId());
                            ps.setInt(3, ((UserLike) value).getFilmId());
                        }
                        if (value instanceof SubscriberStatus) {
                            ps.setInt(1, ((SubscriberStatus) value).getUserId());
                            ps.setInt(2, ((SubscriberStatus) value).getSubscriberId());
                            ps.setString(3, String.valueOf(((SubscriberStatus) value).getFriendshipStatus()));
                            ps.setInt(4, ((SubscriberStatus) value).getUserId());
                        }
                    }
                    public int getBatchSize() {
                        return values.size();
                    }
                });
        conn.commit();
    }
}
