package org.home.yandex.practicum.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    @NotNull
    Integer id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    Date birthday;
}
