package org.home.yandex.practicum.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    @NotNull
    private Integer id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    @NotNull
    private Set<SubscriberStatus> friendsIds;

    public void addNewFriend(SubscriberStatus newFriend) {
        if(friendsIds == null) {
            friendsIds = new HashSet<>();
        }
        friendsIds.add(newFriend);
    }
}
