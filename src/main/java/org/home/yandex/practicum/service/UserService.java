package org.home.yandex.practicum.service;

import org.home.yandex.practicum.model.User;
import org.home.yandex.practicum.storage.InMemoryUserStorage;
import org.home.yandex.practicum.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserStorage userStorage;
    @Autowired
     public UserService(InMemoryUserStorage inMemoryUserStorage) {
         userStorage = inMemoryUserStorage;
     }

     public User addFriend(int id, int friendId) {
        var user = userStorage.getUsers().get(id);
        var newFriendsSet = user.getFriendsIds();
        newFriendsSet.add(friendId);
        user.setFriendsIds(newFriendsSet);
        return user;
     }

     public User removeFriend(int id, int friendId) {
         var user = userStorage.getUsers().get(id);
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
          var user= users.get(friendId);
          if(user != null) {
              userFriends.add(user);
          }
       }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
       var user = userStorage.getUsers().get(id);
       var otherUser = userStorage.getUsers().get(otherId);
       HashSet<Integer> intersection = new HashSet<>();
       Objects.requireNonNull(user).getFriendsIds().forEach((i) -> {
            if (Objects.requireNonNull(otherUser).getFriendsIds().contains(i))
                intersection.add(i);
        });
       List<User> commonFriends = new ArrayList<>();
       for (var friendId : intersection) {
          var friend =  userStorage.getUsers().get(friendId);
           if(friend != null) {
               commonFriends.add(friend);
           }
       }
     return commonFriends;
    }
}
