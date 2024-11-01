package org.home.yandex.practicum.service;

import org.home.yandex.practicum.exceptions.NotFoundException;
import org.home.yandex.practicum.model.SubscriberStatus;
import org.home.yandex.practicum.model.User;
import org.home.yandex.practicum.storage.InMemoryUserStorage;
import org.home.yandex.practicum.storage.UserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.home.yandex.practicum.enums.FriendshipStatus.FRIEND;
import static org.home.yandex.practicum.enums.FriendshipStatus.SUBSCRIBER;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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
        newFriendsSet.add(new SubscriberStatus(friendId, SUBSCRIBER));
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
        var user = userStorage.getUsers().get(id);
        var subscribers = user.getFriendsIds();
        List<User> userFriends = new ArrayList<>();
        for (var subsStatus : subscribers) {
            if (subsStatus.getFriendshipStatus().equals(FRIEND)) {
                var friend = userStorage.getUsers().get(subsStatus.getSubscriberId());
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
        final User user = userStorage.getUsers().get(id);
        final User other = userStorage.getUsers().get(otherId);
        final Set<SubscriberStatus> friends = user.getFriendsIds();
        final Set<SubscriberStatus> otherFriends = other.getFriendsIds();

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userId -> userStorage.getUsers().get(userId.getSubscriberId()))
                .collect(Collectors.toList());
    }

    public User acceptFriend(int id, int friendId) {
        final User user = userStorage.getUsers().get(id);
        var subscribers = user.getFriendsIds();
        subscribers.stream().filter(x->x.getSubscriberId().equals(friendId)).findFirst().ifPresent(x->{
            if(x.getFriendshipStatus().equals(SUBSCRIBER)) {
                x.setFriendshipStatus(FRIEND);
            }
        });
        return user;
    }
}
