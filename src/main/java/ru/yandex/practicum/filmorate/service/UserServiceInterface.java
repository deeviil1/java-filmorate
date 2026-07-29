package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface UserServiceInterface {

    User addUser(User user);

    User deleteUser(Long userId);

    User updateUser(User newUser);

    User getUser(Long userId);

    Collection<User> getAllUser();

    Map<Long, Set<Long>> findAllFriendsList();

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long otherId);
}
