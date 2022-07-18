package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {

    private UserController userController;
    private User user;

    @BeforeEach
    void init() {
        userController = new UserController();
        user = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user.setEmail("email@test.ru");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(birthday);
    }

    @Test
    public void addUserTest() {
        userController.create(user);

        assertEquals(1, userController.getAll().size());
        assertEquals(userController.getAll().get(0), user);
    }


    @Test
    public void addUserWithoutEmail() {
        User user2 = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user2.setLogin("testLogin");
        user2.setName("testName");
        user2.setBirthday(birthday);

        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            userController.update(user2);
        });
        assertEquals(null, thrown.getMessage());

    }

    @Test
    public void addUserWithIncorrectLogin() {
        userController.create(user);
        user.setLogin("test Login");

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.update(user);
        });
        assertEquals("login cannot contain spaces", thrown.getMessage());
    }

    @Test
    public void addUserWithIncorrectBirthday() {
        userController.create(user);
        LocalDate birthday = LocalDate.of(2990, 01, 01);
        user.setBirthday(birthday);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.update(user);
        });
        assertEquals("date of birth cannot be in the future", thrown.getMessage());
    }

    @Test
    public void updateUserTest() {
        userController.create(user);
        user.setName("newTestName");
        userController.update(user);

        assertEquals("newTestName", user.getName());
    }
}
