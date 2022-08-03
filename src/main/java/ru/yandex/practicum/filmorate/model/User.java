package ru.yandex.practicum.filmorate.model;

import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private Set<Long> friends = new HashSet<>();
    @Email(message = "invalid email")
    @NotNull(message = "empty email")
    @NotBlank(message = "empty email")
    private String email;
    @NotBlank(message = "empty login")
    @NotNull(message = "empty")
    private String login;
    private String name;
    @NonNull
    @PastOrPresent(message = "date of birth cannot be in the future")
    private LocalDate birthday;

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        friends.remove(friendId);
    }
}
