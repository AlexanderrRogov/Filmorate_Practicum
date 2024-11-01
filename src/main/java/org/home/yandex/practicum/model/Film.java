package org.home.yandex.practicum.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.home.yandex.practicum.enums.Genre;
import org.home.yandex.practicum.enums.MotionPictureAssociation;

import java.time.LocalDate;
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
    private LocalDate releaseDate;
    @Min(1)
    private Long duration;
    private Set<Integer> userLike;
    private Set<Genre> genre;
    private MotionPictureAssociation mpa;

    public int getRate() {
        return userLike.size();
    }
}
