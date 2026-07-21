package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface{
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> friendList = new HashMap<>();

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User deleteUser(Long userId) {
        return userStorage.deleteUser(userId);
    }

    @Override
    public User updateUser(User newUser) {

        return userStorage.updateUser(newUser);
    }

    @Override
    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public Collection<User> getAllUser() {
        return new ArrayList<>(userStorage.getAllUsers());
    }

    @Override
    public Map<Long, Set<Long>> findAllFriendsList() {
        return new HashMap<>(friendList);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Пользователь не может добавить самого себя в друзья.");
        }

        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (userStorage.getUser(friendId) == null) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден.");
        }

        Set<Long> userFriends = friendList.computeIfAbsent(userId, k -> new HashSet<>());
        Set<Long> friendFriends = friendList.computeIfAbsent(friendId, k -> new HashSet<>());

        userFriends.add(friendId);
        friendFriends.add(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (userStorage.getUser(friendId) == null) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден.");
        }

        Set<Long> userFriends = friendList.get(userId);
        Set<Long> friendFriends = friendList.get(friendId);

        if (userFriends != null) {
            userFriends.remove(friendId);
        }
        if (friendFriends != null) {
            friendFriends.remove(userId);
        }
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        Set<Long> friendIds = friendList.getOrDefault(userId, Collections.emptySet());

        return friendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (userStorage.getUser(otherId) == null) {
            throw new NotFoundException("Пользователь с ID " + otherId + " не найден.");
        }

        Set<Long> userFriends = friendList.getOrDefault(userId, Collections.emptySet());
        Set<Long> otherFriends = friendList.getOrDefault(otherId, Collections.emptySet());

        Set<Long> commonFriendIds = new HashSet<>(userFriends);
        commonFriendIds.retainAll(otherFriends);

        return commonFriendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}