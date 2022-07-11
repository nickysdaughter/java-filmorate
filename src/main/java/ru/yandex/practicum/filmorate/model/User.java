package ru.yandex.practicum.filmorate.model;

import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    @Email(message = "invalid email")
    @NotNull(message = "empty email")
    @NotBlank(message = "empty email")
    private String email;
    @NotBlank(message = "empty login")
    @NotNull(message = "empty")
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
