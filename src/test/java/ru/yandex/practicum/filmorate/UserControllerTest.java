package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {
    private final UserController userController = new UserController();

    private User createTestUser() {
        return User.builder()
                .email("test@mail.ru")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    public void testFindAllMethodWithFilledUsersMap() {
        userController.createUser(createTestUser());
        assertEquals(1, userController.getAllUser().size());
    }

    @Test
    void testCreateUserWithValidData() {
        User user = createTestUser();
        User createdUser = userController.createUser(user);
        assertNotNull(createdUser.getId());
        assertTrue(userController.getAllUser().contains(createdUser));
    }

    @Test
    void testUpdateUserWithValidRequest() {
        User originalUser = userController.createUser(createTestUser());

        originalUser.setEmail("new@email.com");
        originalUser.setName("Updated Name");
        User updatedUser = userController.updateUser(originalUser);

        assertEquals("new@email.com", updatedUser.getEmail());
        assertEquals("Updated Name", updatedUser.getName());
        assertSame(updatedUser, userController.getAllUser()
                .stream()
                .filter(u -> u.getId().equals(originalUser.getId()))
                .findFirst()
                .orElse(null));
    }

    @Test
    void testCreateUserWithSpacesInLogin() {
        User userWithSpaces = User.builder()
                .login("user with spaces")  // логин с пробелами — ключевое условие
                .name("Test User")
                .email("test@mail.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userWithSpaces)
        );

        assertTrue(exception.getMessage().contains("Логин не может содержать пробелы"));
    }

    @Test
    void testUpdateUserWithNullId() {
        User user = userController.createUser(createTestUser());
        user.setId(null);

        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

    @Test
    void testUpdateNonExistentUser() {
        User nonExistent = User.builder()
                .id(999L)
                .login("test")
                .email("test@mail.ru")
                .build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userController.updateUser(nonExistent);
        });
        assertTrue(exception.getMessage().contains("не найден"));
    }

    @Test
    void testUpdateWithInvalidEmail() {
        User existing = userController.createUser(createTestUser());
        existing.setEmail("invalid-email");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.updateUser(existing);
        });
        assertTrue(exception.getMessage().contains("Email должен содержать @"));
    }
}
