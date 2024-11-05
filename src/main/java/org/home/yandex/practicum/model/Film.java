package org.home.yandex.practicum.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate dateProd;
    @Min(1)
    private Long duration;
    private Set<UserLike> userLike;
    @NotNull
    private Set<Genre> genre;
    private String mpa;

    public int getRate() {
        return userLike.size();
    }

    public void addNewGenre(Genre g) {
        if (genre == null) {
            genre = new HashSet<>();
        }
        genre.add(g);
    }

    public void addNewUserLike(UserLike ul) {
        if (userLike == null) {
            userLike = new HashSet<>();
        }
        userLike.add(ul);
    }
}
