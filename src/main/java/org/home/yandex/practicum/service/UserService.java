package org.home.yandex.practicum.service;

import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.model.User;
import org.home.yandex.practicum.storage.InMemoryUserStorage;
import org.home.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    @Autowired
     public UserService(InMemoryUserStorage inMemoryUserStorage) {
         userStorage = inMemoryUserStorage;
     }

     public User addFriend(int id, int friendId) {
        var user = userStorage.getUsers().get(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        var newFriendsSet = user.getFriendsIds();
        newFriendsSet.add(friendId);
        user.setFriendsIds(newFriendsSet);
        return user;
     }

     public User removeFriend(int id, int friendId) {
         var user = userStorage.getUsers().get(id);
         if (user == null) {
             throw new NotFoundException("User not found");
         }
         var newFriendsSet = user.getFriendsIds();
         newFriendsSet.remove(friendId);
         user.setFriendsIds(newFriendsSet);
         return user;
     }

    public List<User> showUserFriends(int id) {
        var friendsIds = userStorage.getUsers().get(id).getFriendsIds();
        var users = userStorage.getUsers();
        List<User> userFriends = new ArrayList<>();
        for (var friendId : friendsIds) {
            var user = users.get(friendId);
            if (user != null) {
                userFriends.add(user);
            }
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        final User user = userStorage.getUsers().get(id);
        final User other = userStorage.getUsers().get(otherId);
        final Set<Integer> friends = user.getFriendsIds();
        final Set<Integer> otherFriends = other.getFriendsIds();

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userId -> userStorage.getUsers().get(userId))
                .collect(Collectors.toList());
    }
}
