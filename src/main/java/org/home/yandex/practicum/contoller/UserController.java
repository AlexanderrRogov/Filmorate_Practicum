package org.home.yandex.practicum.contoller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.home.yandex.practicum.model.User;
import org.home.yandex.practicum.service.UserService;
import org.home.yandex.practicum.storage.InMemoryUserStorage;
import org.home.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
   public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
       userStorage = inMemoryUserStorage;
       this.userService = userService;
   }

    @GetMapping("/users")
    public List<User> users() {
       return userStorage.getUsers().values().stream().toList();
    }

    @PutMapping("/user/{id}")
    public User saveArticle(@Valid @RequestBody User user, @PathVariable int id) {
       return userStorage.saveArticle(user, id);
    }

    @PostMapping("/user")
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @DeleteMapping("/user/{id}")
    public User deleteUser(@PathVariable int id) {
         return userStorage.delete(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
       return userService.showUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getUserFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
