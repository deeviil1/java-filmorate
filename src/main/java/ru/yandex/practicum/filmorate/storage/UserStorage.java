package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    User addUser(User user);

    User deleteUser(Long userId);

    User updateUser(User user);

    User getUser(Long id);

    Collection<User> getAllUsers();

}
