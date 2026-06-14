package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    public final Map<Long, User> users = new HashMap<>();
    public Long nextId = 1L;

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        log.info("Добавление пользователя");

if (user.getLogin().contains(" ")) {
    log.error("Ошибка добавления");
    throw new ValidationException("Логин не может содержать пробелы");
}
if (user.getName() == null || user.getName().isBlank()) {
    log.debug("Имя пользователя не указано — устанавливаем равным логину: {}", user.getLogin());
    user.setName(user.getLogin());
        }

    user.setId(nextId++);
    users.put(user.getId(),user);
    log.info("Пользователь добавлен");
    return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя");

        if (newUser.getId() == null) {
            log.error("ID не может быть null");
            throw new ValidationException("Id не может быть null");
        }

        if (!users.containsKey(newUser.getId())) {
            log.error("Пользователь с ID {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с ID " + newUser.getId() + " не найден");
        }

        if (!newUser.getEmail().contains("@")) {
            log.error("Email не содержит @");
            throw new ValidationException("Email должен содержать @");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            log.debug("Имя пользователя не указано — устанавливаем равным логину: {}", newUser.getLogin());
            newUser.setName(newUser.getLogin());
        }

        users.put(newUser.getId(), newUser);
        return newUser;
    }


    @GetMapping
    public Collection<User> getAllUser(){
        log.info("Список пользователе");
        return users.values();
    }

}
