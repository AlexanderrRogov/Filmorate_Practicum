package org.home.yandex.practicum.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.sql.Date;

@Data
@AllArgsConstructor
public class Film {
    @NotNull
    Integer id;
    @NotBlank
    String name;
    String description;
    Date releaseDate;
    Duration duration;
}
