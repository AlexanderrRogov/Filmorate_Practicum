package org.home.yandex.practicum.storage;

import org.home.yandex.practicum.model.User;

import java.util.List;

public interface UserStorage {
     User update(User user, int id);
     User create(User user);
     User delete(int id);
     List<User> getUsers();
}
