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
        userController.createUser(user);

        assertEquals(1, userController.listOfUsers().size());
        assertEquals(userController.listOfUsers().get(0), user);
    }

    @Test
    public void addUserWithIncorrectEmail() {
        userController.createUser(user);
        user.setEmail("emailtest.ru");

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.updateUser(user);
        });
        assertEquals("email must contain the symbol @", thrown.getMessage());

    }

    @Test
    public void addUserWithoutEmail() {
        User user2 = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user2.setLogin("testLogin");
        user2.setName("testName");
        user2.setBirthday(birthday);

        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            userController.updateUser(user2);
        });
        assertEquals(null, thrown.getMessage());

    }

    @Test
    public void addUserWithEmptyEmail() {
        User user2 = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user2.setEmail("");
        user2.setLogin("testLogin");
        user2.setName("testName");
        user2.setBirthday(birthday);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.createUser(user2);
        });
        assertEquals("email cannot be empty", thrown.getMessage());
    }

    @Test
    public void addUserWithIncorrectLogin() {
        userController.createUser(user);
        user.setLogin("test Login");

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.updateUser(user);
        });
        assertEquals("login cannot contain spaces", thrown.getMessage());
    }

    @Test
    public void addUserWithIncorrectBirthday() {
        userController.createUser(user);
        LocalDate birthday = LocalDate.of(2990, 01, 01);
        user.setBirthday(birthday);

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userController.updateUser(user);
        });
        assertEquals("date of birth cannot be in the future", thrown.getMessage());
    }

    @Test
    public void updateUserTest() {
        userController.createUser(user);
        user.setName("newTestName");
        userController.updateUser(user);

        assertEquals("newTestName", user.getName());
    }
}
