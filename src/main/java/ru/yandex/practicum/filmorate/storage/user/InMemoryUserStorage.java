package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(@Valid User user) {
        Long id = generateId();
        user.setId(id);
        users.put(id, user);
        log.info("User is added: {}", user.getLogin());

        return user;
    }

    @Override
    public User update(@Valid User user) {
        users.put(user.getId(), user);
        log.info("User is updated: {}", user.getLogin());

        return user;
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    private Long generateId() {
        return id++;
    }
}
