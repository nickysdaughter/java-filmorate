package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    private Long id;
    private Set<Long> likes = new HashSet<>();
    @NotBlank(message = "name can't be empty")
    @NotNull(message = "name can't be empty")
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    @Positive(message = "duration can't be negative")
    private int duration;

    public void addLike(Long userId) {

        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);

    }
}
