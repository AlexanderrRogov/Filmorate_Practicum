package org.home.yandex.practicum.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.home.yandex.practicum.enums.FriendshipStatus;

@Data
@AllArgsConstructor
@Builder
public class SubscriberStatus {
    @NotNull
    private  Integer id;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer subscriberId;
    private FriendshipStatus friendshipStatus;

    public SubscriberStatus(Integer userId, Integer subscriberId, FriendshipStatus friendshipStatus) {
        this.userId = userId;
        this.subscriberId = subscriberId;
        this.friendshipStatus = friendshipStatus;
    }
}
