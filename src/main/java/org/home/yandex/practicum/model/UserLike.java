package org.home.yandex.practicum.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserLike {
    @NotNull
    private  Integer id;
    @NotNull
    private Integer filmId;
    @NotNull
    private Integer userId;
}
