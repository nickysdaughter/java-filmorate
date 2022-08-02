package ru.yandex.practicum.filmorate.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        log.debug("User create request: {}", user.getLogin());
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        log.debug("User update request: {}", user.getLogin());
        checkUserExists(user.getId());
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUser(Long id) {
        checkUserExists(id);
        return userStorage.getUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);
        userStorage.getUser(userId).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        checkUserExists(userId);
        checkUserExists(friendId);
        userStorage.getUser(userId).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(userId);
    }

    public List<User> getAllFriends(Long id) {
        checkUserExists(id);
        return userStorage.getAll().stream()
                .filter(user -> userStorage.getUser(id).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        log.debug("Request for common friends of the user {} and the user {}", id, otherId);
        checkUserExists(id);
        checkUserExists(otherId);
        List<Long> userFriendsList = new ArrayList<>(userStorage.getUser(id).getFriends());
        userFriendsList.retainAll(userStorage.getUser(otherId).getFriends());
        return userStorage.getAll().stream()
                .filter(user -> userFriendsList.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
     void checkUserExists(Long id) {
        boolean isNonExistentUser = userStorage.getAll().stream().noneMatch(user -> user.getId().equals(id));
        if (isNonExistentUser) {
            throw new NoSuchElementException("There is no user with such an id");
        }
    }
}
