package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1L;

    @Override
    public User addUser(User user){
        log.info("Добавление пользователя");

        if (user.getLogin().contains(" ")){
            log.error("Ошибка добавления");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()){
            log.debug("Имя пользователя не указано — устанавливаем равным логину: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Long userId){
        if (userId == null){
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        User removedUser = users.remove(userId);
        if (removedUser == null){
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        return removedUser;
    }

    @Override
    public User updateUser(User newUser){
        log.info("Обновление пользователя");

        if (newUser.getId() == null){
            log.error("ID не может быть null");
            throw new ValidationException("Id не может быть null");
        }

        if (!users.containsKey(newUser.getId())){
            log.error("Пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с ID " + newUser.getId() + " не найден");
        }

        if (!newUser.getEmail().contains("@")){
            log.error("Email не содержит @");
            throw new ValidationException("Email должен содержать @");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()){
            log.debug("Имя пользователя не указано — устанавливаем равным логину: {}", newUser.getLogin());
            newUser.setName(newUser.getLogin());
        }

        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUser(Long userId){
        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers(){
        log.info("Список пользователе");
        return new ArrayList<>(users.values());
    }
}
