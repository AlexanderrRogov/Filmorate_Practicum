package org.home.yandex.practicum.storage;

import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.exceptions.ValidationException;
import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();

    public User update(User user, int id) {
        if(users.keySet().stream().toList().contains(user.getId())) {
            users.put(id, user);
        } else {
            log.warn("User {} not found", id);
            throw new ValidationException("User not found");
        }
        return user;
    }

    public User create(User user) {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Email is required");
            throw new ValidationException("Email is required");
        }
        if(user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            log.error("Login is required");
            throw new ValidationException("Login is required");
        }
        if(user.getName().isEmpty()) {
            log.warn("Name is empty. Now login will be your name");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday is after date");
            throw new ValidationException("Correct birthday is required");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(int id) {
        return users.remove(id);
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public Set<SubscriberStatus> getFriendsIds(int userId) {
        return users.get(userId).getFriendsIds();
    }
}
