package org.home.yandex.practicum.dal;

import java.util.List;

public interface DbStorage<T> {
    T update(T value);
    T addParam(int mainId, int id);
    T removeParam(int mainId, int id);
    T create(T value);
    T delete(int id);
    T get(int id);
    List<T> getCollection();

}
