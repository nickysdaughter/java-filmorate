package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    private UserController userController;
    protected static Validator validator;

    @BeforeAll
    public static void BeforeAll() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Autowired
    public UserControllerTests(UserController userController) {
        this.userController = userController;
    }


    @Test
    public void addUserTest() {
        User user = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user.setEmail("email@test.ru");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(birthday);
        userController.create(user);

        assertEquals(1, userController.getAll().size());
        assertEquals(userController.getAll().get(0), user);
    }


    @Test
    public void addUserWithoutEmail() {
        User user = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(birthday);
        userController.create(user);

        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void addUserWithIncorrectLogin() {
        User user = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user.setLogin("test Login");
        user.setName("testName");
        user.setBirthday(birthday);
        userController.create(user);

        assertEquals(1, userController.getAll().size());
    }

    @Test
    public void addUserWithIncorrectBirthday() {
        User user = new User();
        LocalDate birthday = LocalDate.of(1990, 01, 01);
        user.setLogin("test Login");
        user.setName("testName");
        user.setBirthday(birthday);
        userController.create(user);
        LocalDate birthdayInvalid = LocalDate.of(2990, 01, 01);
        user.setBirthday(birthdayInvalid);

        assertEquals(1, userController.getAll().size());
    }
}
