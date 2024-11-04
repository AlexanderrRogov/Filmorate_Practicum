package org.home.yandex.practicum.service;

import org.home.yandex.practicum.dal.DbStorage;
import org.home.yandex.practicum.dal.UserDbStorage;
import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.home.yandex.practicum.enums.FriendshipStatus.FRIEND;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final DbStorage<User> userDbStorage;

    @Autowired
     public UserService(UserDbStorage userDbStorage) {
         this.userDbStorage = userDbStorage;
     }

     public User addFriend(int id, int friendId) {
        var user = userDbStorage.addParam(id, friendId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
     }

     public User removeFriend(int id, int friendId) {
         var user = userDbStorage.removeParam(id, friendId);
         if (user == null) {
             throw new NotFoundException("User not found");
         }
         return user;
     }

    public List<User> showUserFriends(int id) {
        var user = userDbStorage.getCollection().stream().filter(x->x.getId() == id).findFirst().orElseThrow();
        var subscribers = user.getFriendsIds();
        List<User> userFriends = new ArrayList<>();
        for (var subsStatus : subscribers) {
            if (subsStatus.getFriendshipStatus().equals(FRIEND)) {
                var friend = userDbStorage.getCollection().stream().filter(x-> Objects.equals(x.getId(), subsStatus.getId())).findFirst().orElseThrow();
                userFriends.add(friend);
            }
        }
        if(userFriends.isEmpty()) {
            log.warn("No friends found");
            throw new NotFoundException("No friends found");
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        final User user = userDbStorage.getCollection().stream().filter(x->x.getId() == id).findFirst().orElseThrow();
        final User other = userDbStorage.getCollection().stream().filter(x->x.getId() == otherId).findFirst().orElseThrow();
        final Set<SubscriberStatus> friends = user.getFriendsIds();
        final Set<SubscriberStatus> otherFriends = other.getFriendsIds();

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userId -> userDbStorage.getCollection().stream().filter(x-> Objects.equals(x.getId(), userId.getSubscriberId())).findFirst().orElseThrow())
                .collect(Collectors.toList());
    }

    public User acceptFriend(int id, int friendId) {
        return userDbStorage.addParam(id, friendId);
    }

    public List<User> getUsers() {
        return userDbStorage.getCollection();
    }

    public User update(User user) {
      return   userDbStorage.update(user);
    }

    public User create(User user) {
       return userDbStorage.create(user);
    }

    public User delete(int id) {
        return userDbStorage.delete(id);
    }
}
