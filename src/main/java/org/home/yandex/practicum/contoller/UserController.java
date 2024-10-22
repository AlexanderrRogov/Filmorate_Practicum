package org.home.yandex.practicum.contoller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.exceptions.ValidationException;
import org.home.yandex.practicum.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@RestController
@Slf4j
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> users() {
       return users.values().stream().toList();
    }

    @PutMapping("/user/{id}")
    public User saveArticle(@Valid @RequestBody User user, @PathVariable int id) {
        if(users.keySet().stream().toList().contains(user.getId())) {
            users.put(id, user);
        } else {
            log.warn("User {} not found", id);
            throw new ValidationException("User not found");
        }
        return user;
    }

    @PostMapping("/user/{id}")
    public User create(@Valid @RequestBody User user, @PathVariable int id) {
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
        if (user.getBirthday().after(new Date())) {
            log.error("Birthday is after date");
            throw new ValidationException("Correct birthday is required");
        }
       users.put(id, user);
       return user;
    }
}