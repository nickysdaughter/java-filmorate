package ru.yandex.practicum.filmorate.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController extends FilmorateController<User> {

    private Map<Long, User> users = new HashMap<>();
    private Long id = 1L;
    private static LocalDate currentDate = LocalDate.now();

    @Override
    @GetMapping(value = "/users")
    public List<User> getAll() {

        return new ArrayList<>(users.values());
    }

    @Override
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        validation(user);
        Long id = generateId();
        user.setId(id);
        users.put(id, user);
        log.info("User is added: {}", user.getLogin());

        return user;
    }


    @Override
    @SneakyThrows
    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        if (user.getId() < 0) {
            throw new ValidationException("invalid id");
        }
        validation(user);
        users.put(user.getId(), user);
        log.info("User is updated: {}", user.getLogin());

        return user;
    }

    private Long generateId() {
        return id++;
    }

    @SneakyThrows
    protected static void validation(User user) {

        String email = user.getEmail();
//        final Pattern VALID_EMAIL_ADDRESS_REGEX =
//                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
//        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        try {


            if (email.equals("")) {
                log.debug("email is empty");
                throw new ValidationException("email cannot be empty");
            }
//        if (matcher.find()) {
//            log.debug("email is invalid: {}", user.getEmail());
//            throw new ValidationException("email must contain the symbol @");
//        }
            if (!email.contains("@")) {
                log.debug("email is invalid: {}", user.getEmail());
                throw new ValidationException("email must contain the symbol @");
            }
            if (user.getLogin().contains(" ")) {
                log.debug("login is contain spaces: {}", user.getLogin());
                throw new ValidationException("login cannot contain spaces");
            }
            if (user.getName() == null || user.getName().equals("")) {
                log.info("name can be empty — in this case, the login will be used");
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(currentDate)) {
                log.info("date of birth is in the future: {}", user.getBirthday());
                throw new ValidationException("date of birth cannot be in the future");
            }
            if (user.getLogin() == null) {
                log.info("user must have a login");
                throw new ValidationException("empty login");
            }
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}
