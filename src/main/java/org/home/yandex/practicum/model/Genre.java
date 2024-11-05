package org.home.yandex.practicum.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Genre {
    @NotNull
    private  Integer id;
    @NotNull
    private Integer filmId;
    @NotNull
    private org.home.yandex.practicum.enums.Genre filmGenre;
}
