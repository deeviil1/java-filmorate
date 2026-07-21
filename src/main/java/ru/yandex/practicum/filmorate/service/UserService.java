package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService{
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> friendList = new HashMap<>();

    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public User addUser(User user){
      return  userStorage.addUser(user);
    }
     public User deleteUser(Long userId){
        return userStorage.deleteUser(userId);
     }

     public User updateUser(User newUser){

        return userStorage.updateUser(newUser);
     }

     public User getUser(Long userId){
        return userStorage.getUser(userId);
     }

     public Collection<User> getAllUser(){
        return new ArrayList<>(userStorage.getAllUsers());
     }


    public Map<Long, Set<Long>> findAllFriendsList(){
        return new HashMap<>(friendList);
    }

    public void addFriend(Long userId, Long friendId){
        if (userId.equals(friendId)) {
            throw new ValidationException("Пользователь не может добавить самого себя в друзья.");
        }

        Set<Long> userFriends = friendList.computeIfAbsent(userId, k -> new HashSet<>());
        Set<Long> friendFriends = friendList.computeIfAbsent(friendId, k -> new HashSet<>());

        userFriends.add(friendId);
        friendFriends.add(userId);
    }


    public void removeFriend(Long userId, Long friendId){
        Set<Long> userFriends = friendList.get(userId);
        Set<Long> friendFriends = friendList.get(friendId);

        if (userFriends != null){
            userFriends.remove(friendId);
        }
        if (friendFriends != null){
            friendFriends.remove(userId);
        }
    }
    public Collection<User> getFriends(Long userId){
        Set<Long> friendIds = friendList.getOrDefault(userId, Collections.emptySet());

        return friendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
    public Collection<User> getCommonFriends(Long userId, Long otherId){
        Set<Long> userFriends = friendList.getOrDefault(userId, Collections.emptySet());
        Set<Long> otherFriends = friendList.getOrDefault(otherId, Collections.emptySet());


        Set<Long> commonFriendIds = new HashSet<>(userFriends);
        commonFriendIds.retainAll(otherFriends);


        return commonFriendIds.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

}