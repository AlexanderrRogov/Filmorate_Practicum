package org.home.yandex.practicum.storage;

import org.home.yandex.practicum.model.User;

import java.util.HashMap;
import java.util.Set;

public interface UserStorage {
     User saveArticle(User user, int id);
     User create(User user);
     User delete(int id);
     HashMap<Integer, User> getUsers();
     Set<Integer> getFriendsIds(int id);
}
